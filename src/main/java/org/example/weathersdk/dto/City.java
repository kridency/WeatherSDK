package org.example.weathersdk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.TimeZone;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
    private String id;
    private String name;
    private Coord coord;
    private String country;
    private long population;
    private TimeZone timezone;
    private Instant sunrise;
    private Instant sunset;
}
