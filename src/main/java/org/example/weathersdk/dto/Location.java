package org.example.weathersdk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.weathersdk.constant.Country;

import java.util.Map;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    private String name;
    @JsonProperty("local_names")
    private Map<String, String> localNames;
    private float lat;
    private float lon;
    private Country country;
    private String state;
}
