package com.test.myproject.infrastructure.repositories;

import com.test.myproject.infrastructure.interfaces.IWeatherMapRepository;
import com.test.myproject.infrastructure.openweathermap.OpenWeatherMap;
import com.test.myproject.infrastructure.openweathermap.OpenWeatherMapParameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
@Getter
@Setter
public class WeatherMapRepository implements IWeatherMapRepository {

    private final String HOST = "https://api.openweathermap.org";
    private final String ENDPOINT = "/data/2.5/weather";

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Autowired
    private final RestTemplate restTemplate;

    @Override
    public OpenWeatherMap findWeather(OpenWeatherMapParameter openWeatherMapParameter) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(HOST + ENDPOINT);

        builder.queryParam("appid", this.getApiKey());
        builder.queryParam("lat", openWeatherMapParameter.getLat());
        builder.queryParam("lon", openWeatherMapParameter.getLon());
        builder.queryParam("mode", openWeatherMapParameter.getMode());
        builder.queryParam("units", openWeatherMapParameter.getUnits());
        if (openWeatherMapParameter.getLang() != null) {
            builder.queryParam("lang", openWeatherMapParameter.getLang());
        }
        String uri = builder.build().encode().toUriString();
        log.info("Send request, uri : {}", uri);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<OpenWeatherMap> response = this.restTemplate.exchange(uri,
                HttpMethod.GET, request,
                OpenWeatherMap.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            System.out.println("got response successfully");
        }
        return response.getBody();
    }
}
