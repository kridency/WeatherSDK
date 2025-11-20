package org.example.weathersdk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Weather {
    @JsonIgnore
    private int id;
    private String main;
    private String description;
    @JsonIgnore
    private String icon;
}
