package com.test.myproject.infrastructure.openweathermap;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherMap implements Serializable {
    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private Clouds clouds;
    private Integer dt;
    private Sys sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;
}