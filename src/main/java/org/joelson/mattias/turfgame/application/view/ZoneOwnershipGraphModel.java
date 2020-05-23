package org.joelson.mattias.turfgame.application.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.joelson.mattias.turfgame.application.model.TakeData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.VisitCollection;

import java.awt.Container;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ZoneOwnershipGraphModel {

    private static class ZoneOwnershipData {

        private final Instant when;
        private final int zoneCount;
        private final int zonePphCount;

        private ZoneOwnershipData(Instant when, int zoneCount, int zonePphCount) {
            this.when = when;
            this.zoneCount = zoneCount;
            this.zonePphCount = zonePphCount;
        }

        public Instant getWhen() {
            return when;
        }

        public int getZoneCount() {
            return zoneCount;
        }

        public int getZonePphCount() {
            return zonePphCount;
        }
    }

    private final VisitCollection visits;
    private List<ZoneOwnershipData> currentZoneOwnership;
    private JFreeChart chart;

    public ZoneOwnershipGraphModel(VisitCollection visits, UserData selectedUser) {
        this.visits = visits;
        updateSelectedUser(selectedUser);
    }

    public void updateSelectedUser(UserData selectedUser) {
        List<TakeData> takes = this.visits.getVisits(selectedUser).stream()
                .filter(visitData -> visitData instanceof TakeData)
                .map(visitData -> (TakeData) visitData)
                .collect(Collectors.toList());
        List<ZoneOwnershipData> changes = new ArrayList<>(takes.size() * 2);
        for (TakeData take : takes) {
            changes.add(new ZoneOwnershipData(take.getWhen(), 1, take.getPph()));
            if (!take.isOwning()) {
                changes.add(new ZoneOwnershipData(take.getWhen().plusSeconds(take.getDuration().getSeconds()), -1, -take.getPph()));
            }
        }
        changes.sort(Comparator.comparing(ZoneOwnershipData::getWhen));
        currentZoneOwnership = new ArrayList<>(changes.size());
        Instant last = null;
        int sumZones = 0;
        int sumPph = 0;
        for (int i = 0; i < changes.size(); i += 1) {
            ZoneOwnershipData data = changes.get(i);
            if (last == null || last.isBefore(data.getWhen())) {
                if (last != null) {
                    currentZoneOwnership.add(new ZoneOwnershipData(last, sumZones, sumPph));
                }
                last = data.getWhen();
                sumZones += data.getZoneCount();
                sumPph += data.getZonePphCount();
            }
        }
        if (last != null) {
            currentZoneOwnership.add(new ZoneOwnershipData(last, sumZones, sumPph));
        }
        if (chart != null) {
            chart.getXYPlot().setDataset(getDataSet());
            updateTimeAxis();
        }
    }

    public Container getChart() {
        chart = ChartFactory.createXYLineChart("Zone Ownership", "When", "Sum", getDataSet());
        updateTimeAxis();
        return new ChartPanel(chart);
    }

    private XYDataset getDataSet() {
        TimeSeries zones = new TimeSeries("Zones");
        TimeSeries pph = new TimeSeries("PPH");
        if (!currentZoneOwnership.isEmpty()) {
            for (ZoneOwnershipData zoneOwnership : currentZoneOwnership) {
                zones.add(getTime(zoneOwnership.getWhen()), zoneOwnership.getZoneCount());
                pph.add(getTime(zoneOwnership.getWhen()), zoneOwnership.getZonePphCount());
            }
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(zones);
        dataset.addSeries(pph);
        return dataset;
    }

    private void updateTimeAxis() {
        if (!currentZoneOwnership.isEmpty()) {
            DateAxis xAxis = new DateAxis();
            xAxis.setRange(getTime(currentZoneOwnership.get(0).getWhen()).getFirstMillisecond(),
                    getTime(currentZoneOwnership.get(currentZoneOwnership.size() - 1).getWhen()).getLastMillisecond());
            chart.getXYPlot().setDomainAxis(xAxis);
        }
    }

    private static RegularTimePeriod getTime(Instant when) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(when, ZoneId.systemDefault());
        return new Second(dateTime.getSecond(), dateTime.getMinute(), dateTime.getHour(), dateTime.getDayOfMonth(), dateTime.getMonthValue(), dateTime.getYear());
    }
}
