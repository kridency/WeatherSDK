package org.example.weathersdk.constant;

public enum Endpoint {
    CURRENT("/weather"),
    FORECAST("/forecast"),
    CITY_NAME("/direct"),
    CITY_ZIP("/zip"),
    CITY_COORDS("/reverse"),
    DATA_URL("https://api.openweathermap.org/data"),
    GEO_URL("https://api.openweathermap.org/geo");

    private final String endpoint;

    Endpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return endpoint;
    }
}
