package org.oleggalimov.dbmonitoring.back.enumerations;

public enum InfluxTimePeriod {
    LAST_5M("now() - 5m"),
    LAST_10M("now() - 10m"),
    LAST_15M("now() - 15m"),
    LAST_30M("now() - 30m"),
    LAST_1H("now() - 1h");

    private String period;

    private InfluxTimePeriod(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }
}
