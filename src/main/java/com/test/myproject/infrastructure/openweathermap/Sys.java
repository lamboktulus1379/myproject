package com.test.myproject.infrastructure.openweathermap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sys {
    private int type;
    private int id;
    private double message;
    private String country;
    private int sunrise;
    private int sunset;
}
