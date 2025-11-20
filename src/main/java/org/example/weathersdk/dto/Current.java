package org.example.weathersdk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Current {
    private float temp;
    @JsonProperty("feels_like")
    private float feelsLike;
    @JsonIgnore
    private float pressure;
    @JsonIgnore
    private float humidity;
    @JsonIgnore
    @JsonProperty("temp_min")
    private float tempMin;
    @JsonIgnore
    @JsonProperty("temp_max")
    private float tempMax;
    @JsonIgnore
    @JsonProperty("sea_level")
    private float seaLevel;
    @JsonIgnore
    @JsonProperty("grnd_level")
    private float grndLevel;
    @JsonIgnore
    @JsonProperty("temp_kf")
    private String tempKf;
}
