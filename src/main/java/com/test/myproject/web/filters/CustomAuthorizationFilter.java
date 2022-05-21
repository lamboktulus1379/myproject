package com.test.myproject.web.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.myproject.core.interfaces.IUserService;
import com.test.myproject.core.user_aggreate.User;
import com.test.myproject.core.user_aggreate.advice.UserAdvice;
import com.test.myproject.web.utils.JWTToken;
import com.test.myproject.infrastructure.openweathermap.OpenWeatherMap;
import com.test.myproject.infrastructure.openweathermap.OpenWeatherMapParameter;
import com.test.myproject.infrastructure.interfaces.IWeatherMapRepository;
import com.test.myproject.web.api_models.Error;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final IWeatherMapRepository weatherMapRepository;
    private final IUserService userService;

    public CustomAuthorizationFilter(IUserService userService, IWeatherMapRepository weatherMapRepository) {
        this.userService = userService;
        this.weatherMapRepository = weatherMapRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")
                || request.getServletPath().equals("/api/register") || request.getServletPath().equals("/health")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());

                    JWTToken jwtToken = new JWTToken();
                    DecodedJWT decodedJWT = jwtToken.verifyToken(token);

                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Double lat = decodedJWT.getClaim("lat").asDouble();
                    Double lon = decodedJWT.getClaim("lon").asDouble();

                    response.setHeader("weather", null);
                    if (lat != 0 && lon != 0) {
                        OpenWeatherMapParameter openWeatherMapParameter = new OpenWeatherMapParameter();
                        openWeatherMapParameter.setLat(lat);
                        openWeatherMapParameter.setLon(lon);
                        OpenWeatherMap openWeatherMap = weatherMapRepository.findWeather(openWeatherMapParameter);
                        log.info("Get weather ... " + openWeatherMap.toString());

                        String openWeatherMapString = new ObjectMapper()
                                .writeValueAsString(openWeatherMap);
                        log.info("Open Weather Response", openWeatherMapString);
                        response.setHeader("weather", openWeatherMapString);
                    }

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for (String role : roles) {
                        authorities.add(new SimpleGrantedAuthority(role));
                    }

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);

                    // Get User Fullname
                    User user = userService.getUserByEmail(username);
                    response.setHeader("fullname", user.getFullname());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    log.error("Error logging in: {}", exception.getMessage());

                    Map<String, Error> mapError = UserAdvice.GenerateUserAdvice(403, exception.getMessage());
                    response.setContentType("application/json");

                    new ObjectMapper().writeValue(response.getOutputStream(), mapError);
                }
            } else {
                Map<String, Error> mapError = UserAdvice.GenerateUserAdvice(403, "Please provide a valid token.");

                response.setContentType("application/json");
                response.setStatus(403);

                new ObjectMapper().writeValue(response.getOutputStream(), mapError);
            }
        }
    }

}
