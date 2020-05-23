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
import org.joelson.mattias.turfgame.application.model.RevisitData;
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.VisitCollection;
import org.joelson.mattias.turfgame.application.model.VisitData;

import java.awt.Container;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ZoneTakeGraphModel {

    private static class ZoneTakesData {

        private final Instant when;
        private final int zoneCount;

        private ZoneTakesData(Instant when, int zoneCount) {
            this.when = when;
            this.zoneCount = zoneCount;
        }

        public Instant getWhen() {
            return when;
        }

        public int getZoneCount() {
            return zoneCount;
        }
    }

    private final VisitCollection visits;
    private List<ZoneTakesData> currentZoneTake;
    private JFreeChart chart;

    public ZoneTakeGraphModel(VisitCollection visits, UserData selectedUser) {
        this.visits = visits;
        updateSelectedUser(selectedUser);
    }

    public void updateSelectedUser(UserData selectedUser) {
        List<VisitData> visits = this.visits.getVisits(selectedUser).stream()
                .filter(visitData -> !(visitData instanceof RevisitData))
                .collect(Collectors.toList());
        int minusI = 0;
        int zones = 0;
        currentZoneTake = new ArrayList<>(visits.size());
        for (VisitData visit : visits) {
            Instant minusWhen = visits.get(minusI).getWhen().plus(1, ChronoUnit.DAYS);
            while (minusWhen.isBefore(visit.getWhen())) {
                zones -= 1;
                currentZoneTake.add(new ZoneTakesData(minusWhen, zones));
                minusI += 1;
                minusWhen = visits.get(minusI).getWhen().plus(1, ChronoUnit.DAYS);
            }
            if (visit.getWhen().isBefore(minusWhen)) {
                zones += 1;
            } else {
                minusI += 1;
            }
            currentZoneTake.add(new ZoneTakesData(visit.getWhen(), zones));
        }
        Instant now = Instant.now();
        for (int i = minusI; i < visits.size(); i += 1) {
            Instant when = visits.get(i).getWhen().plus(1, ChronoUnit.DAYS);
            if (when.isAfter(now)) {
                break;
            }
            zones -= 1;
            currentZoneTake.add(new ZoneTakesData(when, zones));
        }
        if (chart != null) {
            chart.getXYPlot().setDataset(getDataSet());
            updateTimeAxis();
        }
    }

    public Container getChart() {
        chart = ChartFactory.createXYLineChart("Zone takes over 24 h window", "When", "Sum", getDataSet());
        updateTimeAxis();
        return new ChartPanel(chart);
    }

    private XYDataset getDataSet() {
        TimeSeries zones = new TimeSeries("Zones");
        if (!currentZoneTake.isEmpty()) {
            for (ZoneTakesData zoneOwnership : currentZoneTake) {
                zones.add(getTime(zoneOwnership.getWhen()), zoneOwnership.getZoneCount());
            }
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(zones);
        return dataset;
    }

    private void updateTimeAxis() {
        if (!currentZoneTake.isEmpty()) {
            DateAxis xAxis = new DateAxis();
            xAxis.setRange(getTime(currentZoneTake.get(0).getWhen()).getFirstMillisecond(),
                    getTime(currentZoneTake.get(currentZoneTake.size() - 1).getWhen()).getLastMillisecond());
            chart.getXYPlot().setDomainAxis(xAxis);
        }
    }

    private static RegularTimePeriod getTime(Instant when) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(when, ZoneId.systemDefault());
        return new Second(dateTime.getSecond(), dateTime.getMinute(), dateTime.getHour(), dateTime.getDayOfMonth(), dateTime.getMonthValue(), dateTime.getYear());
    }}
