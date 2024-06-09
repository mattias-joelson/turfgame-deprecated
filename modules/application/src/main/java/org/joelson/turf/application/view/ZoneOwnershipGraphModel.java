package org.joelson.turf.application.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.joelson.turf.application.model.TakeData;
import org.joelson.turf.application.model.UserData;
import org.joelson.turf.application.model.VisitCollection;

import java.awt.Container;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ZoneOwnershipGraphModel {

    private final VisitCollection visits;
    private List<ZoneOwnershipData> currentZoneOwnership;
    private JFreeChart chart;
    public ZoneOwnershipGraphModel(VisitCollection visits, UserData selectedUser) {
        this.visits = visits;
        updateSelectedUser(selectedUser);
    }

    private static RegularTimePeriod getTime(Instant when) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(when, ZoneId.systemDefault());
        return new Second(dateTime.getSecond(), dateTime.getMinute(), dateTime.getHour(), dateTime.getDayOfMonth(),
                dateTime.getMonthValue(), dateTime.getYear());
    }

    public void updateSelectedUser(UserData selectedUser) {
        List<TakeData> takes = this.visits.getVisits(selectedUser).stream()
                .filter(visitData -> visitData instanceof TakeData).map(visitData -> (TakeData) visitData).toList();
        List<ZoneOwnershipData> changes = new ArrayList<>(takes.size() * 2);
        for (TakeData take : takes) {
            changes.add(new ZoneOwnershipData(take.getWhen(), 1, take.getPph()));
            if (!take.isOwning()) {
                changes.add(new ZoneOwnershipData(take.getWhen().plusSeconds(take.getDuration().getSeconds()), -1,
                        -take.getPph()));
            }
        }
        changes.sort(Comparator.comparing(ZoneOwnershipData::when));
        currentZoneOwnership = new ArrayList<>(changes.size());
        Instant last = null;
        int sumZones = 0;
        int sumPph = 0;
        for (ZoneOwnershipData data : changes) {
            if (last == null || last.isBefore(data.when())) {
                if (last != null) {
                    currentZoneOwnership.add(new ZoneOwnershipData(last, sumZones, sumPph));
                }
                last = data.when();
                sumZones += data.zoneCount();
                sumPph += data.zonePphCount();
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

    public void updateSelectedUserNew(UserData selectedUser) {
        List<TakeData> takes = visits.getAllVisits().stream().filter(visitData -> visitData instanceof TakeData)
                .map(visitData -> (TakeData) visitData).sorted(Comparator.comparing(TakeData::getWhen)).toList();
        Set<String> ownedZones = new HashSet<>();
        List<ZoneOwnershipData> changes = new ArrayList<>();
        for (TakeData take : takes) {
            if (take.getTaker().equals(selectedUser)) {
                changes.add(new ZoneOwnershipData(take.getWhen(), 1, take.getPph()));
                ownedZones.add(take.getZone().getName());
            } else if (ownedZones.contains(take.getZone().getName())) {
                changes.add(new ZoneOwnershipData(take.getWhen(), -1, -take.getPph()));
                ownedZones.remove(take.getZone().getName());
            }
        }
        // med mera
        if (chart != null) {
            chart.getXYPlot().setDataset(getDataSet());
            updateTimeAxis();
        }
    }

    public Container getChart() {
        chart = ChartFactory.createXYLineChart("Zone Ownership", "When", "Sum", getDataSet());
        StandardXYToolTipGenerator standardXYToolTipGenerator = new StandardXYToolTipGenerator("{0}: {2} @ {1}",
                new SimpleDateFormat("yyyy-MM-dd HH:mm"), new DecimalFormat("0"));
        chart.getXYPlot().getRenderer().setDefaultToolTipGenerator(standardXYToolTipGenerator);
        updateTimeAxis();
        return new ChartPanel(chart);
    }

    private XYDataset getDataSet() {
        TimeSeries zones = new TimeSeries("Zones");
        TimeSeries pph = new TimeSeries("PPH");
        if (!currentZoneOwnership.isEmpty()) {
            for (ZoneOwnershipData zoneOwnership : currentZoneOwnership) {
                zones.add(getTime(zoneOwnership.when()), zoneOwnership.zoneCount());
                pph.add(getTime(zoneOwnership.when()), zoneOwnership.zonePphCount());
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
            xAxis.setRange(getTime(currentZoneOwnership.get(0).when()).getFirstMillisecond(),
                    getTime(currentZoneOwnership.get(currentZoneOwnership.size() - 1).when()).getLastMillisecond());
            chart.getXYPlot().setDomainAxis(xAxis);
        }
    }

    private record ZoneOwnershipData(Instant when, int zoneCount, int zonePphCount) {

    }
}
