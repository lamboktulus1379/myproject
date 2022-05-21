package com.test.myproject.web.api;

import com.test.myproject.infrastructure.openweathermap.Mode;
import com.test.myproject.infrastructure.openweathermap.OpenWeatherMap;
import com.test.myproject.infrastructure.openweathermap.OpenWeatherMapParameter;
import com.test.myproject.infrastructure.openweathermap.Units;
import com.test.myproject.infrastructure.interfaces.IWeatherMapRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WeatherController {

    @Autowired
    public IWeatherMapRepository weatherMapRepository;

    @GetMapping("/weathers")
    public OpenWeatherMap getWeather(@RequestParam() double lat, @RequestParam() double lon,
            @RequestParam(required = false, defaultValue = "json") String mode,
            @RequestParam(required = false, defaultValue = "metric") String units,
            @RequestParam(required = false) String lang) {
        OpenWeatherMapParameter openWeatherMapParameter = new OpenWeatherMapParameter();
        openWeatherMapParameter.setLat(lat);
        openWeatherMapParameter.setLon(lon);

        switch (mode) {
            case "xml":
                openWeatherMapParameter.setMode(Mode.xml);
                break;
            case "html":
                openWeatherMapParameter.setMode(Mode.html);
            default:
                openWeatherMapParameter.setMode(Mode.json);
                break;
        }

        switch (units) {
            case "standard":
                openWeatherMapParameter.setUnits(Units.standard);
                break;
            case "imperial":
                openWeatherMapParameter.setUnits(Units.imperial);
            default:
                openWeatherMapParameter.setUnits(Units.metric);
                break;
        }

        if (units != null) {
            openWeatherMapParameter.setUnits(Units.valueOf(units));
        }
        if (lang != null) {
            openWeatherMapParameter.setLang(lang);
        }
        OpenWeatherMap openWeatherMap = weatherMapRepository.findWeather(openWeatherMapParameter);

        return openWeatherMap;
    }
}
