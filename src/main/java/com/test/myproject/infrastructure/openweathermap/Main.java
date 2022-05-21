package com.test.myproject.infrastructure.openweathermap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Main {
    private double temp;
    @JsonProperty(value = "feels_like")
    private double feelsLike;
    @JsonProperty(value = "temp_min")
    private double tempMin;
    @JsonProperty(value = "temp_max")
    private double tempMax;
    private Integer pressure;
    private Integer humidity;
}
