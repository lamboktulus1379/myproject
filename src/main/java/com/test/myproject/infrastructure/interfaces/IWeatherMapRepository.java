package com.test.myproject.infrastructure.interfaces;

import com.test.myproject.infrastructure.openweathermap.OpenWeatherMap;
import com.test.myproject.infrastructure.openweathermap.OpenWeatherMapParameter;

import org.springframework.stereotype.Repository;

@Repository
public interface IWeatherMapRepository {
    OpenWeatherMap findWeather(OpenWeatherMapParameter openWeatherMapParameter);
}
