package org.example.weathersdk.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sys {
    @JsonIgnore
    private String type;
    @JsonIgnore
    private long id;
    @JsonIgnore
    private String message;
    @JsonIgnore
    private String country;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Instant sunrise;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Instant sunset;
    @JsonIgnore
    private char pod;
}
