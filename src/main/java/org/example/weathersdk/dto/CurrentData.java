package org.example.weathersdk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Collection;
import java.util.TimeZone;

@Data
@NoArgsConstructor
public class CurrentData {
    private Coord coord;
    private Collection<Weather> weather;
    private String base;
    private Current main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private Rain rain;
    private Snow snow;
    private Instant dt;
    private Sys sys;
    private TimeZone timezone;
    private int id;
    private String name;
    private int cod;
}
