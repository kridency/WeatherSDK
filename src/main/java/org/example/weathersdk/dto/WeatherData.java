package org.example.weathersdk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherData {
    private Weather weather;
    private Current temperature;
    private int visibility;
    private Wind wind;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Instant datetime;
    private Sys sys;
    private int timezone;
    private String name;
}
