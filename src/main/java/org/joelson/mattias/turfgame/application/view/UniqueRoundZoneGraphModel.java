package org.joelson.mattias.turfgame.application.view;

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
import org.joelson.mattias.turfgame.application.model.UserData;
import org.joelson.mattias.turfgame.application.model.VisitCollection;
import org.joelson.mattias.turfgame.application.model.VisitData;

import java.awt.Container;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class UniqueRoundZoneGraphModel {

    private static final class UniqueZoneData {

        private final String zoneName;
        private final Instant when;
        private final int number;

        private UniqueZoneData(String zoneName, Instant when, int number) {
            this.zoneName = zoneName;
            this.when = when;
            this.number = number;
        }

        public String getZoneName() {
            return zoneName;
        }

        public Instant getWhen() {
            return when;
        }

        public int getNumber() {
            return number;
        }
    }

    private final transient VisitCollection visits;
    private ArrayList<UniqueZoneData> currentUniqueZones;
    private JFreeChart chart;

    // TODO does not handle rounds at all
    public UniqueRoundZoneGraphModel(VisitCollection visits, UserData selectedUser) {
        this.visits = visits;
        updateSelectedUser(selectedUser);
    }

    public void updateSelectedUser(UserData selectedUser) {
        List<VisitData> userVisits = this.visits.getVisits(selectedUser).stream()
                .sorted(Comparator.comparing(VisitData::getWhen))
                .collect(Collectors.toList());
        Set<String> zoneNames = new HashSet<>(userVisits.size());
        currentUniqueZones = new ArrayList<>(10);
        int number = 0;
        for (VisitData visit : userVisits) {
            String zoneName = visit.getZone().getName();
            if (!zoneNames.contains(zoneName)) {
                number += 1;
                currentUniqueZones.add(new UniqueZoneData(zoneName, visit.getWhen(), number));
                zoneNames.add(zoneName);
            }
        }
        if (chart != null) {
            chart.getXYPlot().setDataset(getDataSet());
            updateTimeAxis();
        }
    }

    public Container getChart() {
        chart = ChartFactory.createXYLineChart("Unique Round Zones", "When", "Count", getDataSet());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE.withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME.withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());
        StandardXYToolTipGenerator standardXYToolTipGenerator = new StandardXYToolTipGenerator() {
            @Override
            public String generateLabelString(XYDataset dataset, int series, int item) {
                UniqueZoneData uniqueZoneData = currentUniqueZones.get(item);
                return String.format("%d - %s @ %s %s", item, uniqueZoneData.getZoneName(), dateFormatter.format(uniqueZoneData.getWhen()),
                        timeFormatter.format(uniqueZoneData.getWhen()));
            }
        };
        chart.getXYPlot().getRenderer().setDefaultToolTipGenerator(standardXYToolTipGenerator);
        updateTimeAxis();
        return new ChartPanel(chart);
    }

    private XYDataset getDataSet() {
        TimeSeries zones = new TimeSeries("Zones");
        if (!currentUniqueZones.isEmpty()) {
            for (UniqueZoneData uniqueZone : currentUniqueZones) {
                zones.add(getTime(uniqueZone.getWhen()), uniqueZone.getNumber());
            }
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(zones);
        return dataset;
    }

    private void updateTimeAxis() {
        if (!currentUniqueZones.isEmpty()) {
            DateAxis xAxis = new DateAxis();
            xAxis.setRange(getTime(currentUniqueZones.get(0).getWhen()).getFirstMillisecond(),
                    getTime(currentUniqueZones.get(currentUniqueZones.size() - 1).getWhen()).getLastMillisecond());
            chart.getXYPlot().setDomainAxis(xAxis);
        }
    }

    private static RegularTimePeriod getTime(Instant when) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(when, ZoneId.systemDefault());
        return new Second(dateTime.getSecond(), dateTime.getMinute(), dateTime.getHour(), dateTime.getDayOfMonth(), dateTime.getMonthValue(), dateTime.getYear());
    }
}
