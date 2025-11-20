package org.example.weathersdk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Collection;

@Data
@NoArgsConstructor
public class Forecast {
    private Instant dt;
    private Current main;
    private Collection<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private int visibility;
    private float pop;
    private Rain rain;
    private Snow snow;
    private Sys sys;
    @JsonProperty("dt_txt")
    private String dtTxt;
}
