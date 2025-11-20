package org.example.weathersdk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Wind {
    private float speed;
    @JsonIgnore
    private float deg;
    @JsonIgnore
    private float gust;
}
