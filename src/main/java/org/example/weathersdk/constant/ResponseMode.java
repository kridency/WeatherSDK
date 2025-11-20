package org.example.weathersdk.constant;

public enum ResponseMode {
    XML("xml"),
    HTML("html");

    private final String mode;

    ResponseMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return mode;
    }
}
