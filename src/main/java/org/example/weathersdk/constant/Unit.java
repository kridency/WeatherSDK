package org.example.weathersdk.constant;

public enum Unit {
    STANDARD("standard"),
    METRIC("metric"),
    IMPERIAL("imperial");

    private final String unit;

    Unit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return unit;
    }
}
