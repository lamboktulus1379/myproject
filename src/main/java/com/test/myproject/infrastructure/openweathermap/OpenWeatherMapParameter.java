package com.test.myproject.infrastructure.openweathermap;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenWeatherMapParameter {
    private double lat;
    private double lon;
    @Value("${openweathermap.api.key")
    private String appid;
    private Mode mode = Mode.json;
    private Units units = Units.metric;
    private String lang;

    @Override
    public String toString() {
        return "Loaded Api Key..." + appid;
    }
}
