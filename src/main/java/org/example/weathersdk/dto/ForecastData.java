package org.example.weathersdk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class ForecastData {
    private int cod;
    private String message;
    private int cnt;
    private Collection<Forecast> list;
    private City city;
}
