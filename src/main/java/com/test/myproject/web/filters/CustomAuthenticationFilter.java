package com.test.myproject.web.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.myproject.core.user_aggreate.advice.UserAdvice;
import com.test.myproject.web.utils.JWTToken;
import com.test.myproject.web.utils.Token;
import com.test.myproject.web.utils.TokenParameter;
import com.test.myproject.web.api_models.Error;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
        private final AuthenticationManager authenticationManager;

        private double lat;
        private double lon;

        public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
                this.authenticationManager = authenticationManager;
        }

        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                        throws AuthenticationException {
                String username = request.getParameter("email");
                String password = request.getParameter("password");

                log.info("Username is: {}", username);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                username, password);
                return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }

        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                        FilterChain chain,
                        Authentication authResult) throws IOException, ServletException {
                User user = (User) authResult.getPrincipal();
                TokenParameter tokenParameter = new TokenParameter();
                JWTToken jwtToken = new JWTToken();

                Double lat = Double.parseDouble(request.getHeader("lat"));
                Double lon = Double.parseDouble(request.getHeader("lon"));

                if (lat != 0 && lon != 0) {
                        this.setLat(lat);
                        this.setLon(lon);

                        log.info("Latitude is: {}", this.getLat());
                        log.info("Longitude is: {}", this.getLon());

                        tokenParameter.setLat(lat);
                        tokenParameter.setLon(lon);
                }

                Token token = jwtToken.generateToken(user, tokenParameter);

                // com.test.myproject.core.user_aggreate.User userLogin =
                // userService.getUserByEmail(user.getUsername());

                // OpenWeatherMapParameter openWeatherMapParameter = new
                // OpenWeatherMapParameter();
                // openWeatherMapParameter.setLat(this.getLat());
                // openWeatherMapParameter.setLon(this.getLon());
                // OpenWeatherMap openWeatherMap =
                // weatherMapRepository.findWeather(openWeatherMapParameter);
                // log.info("Get weather ... " + openWeatherMap.toString());

                // double weather = openWeatherMap.getMain().getTemp();
                // response.setHeader("fullname", userLogin.getFullname());
                // response.setHeader("weather", String.valueOf(weather));

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", token.getAccessToken());
                tokens.put("refreshToken", token.getRefreshToken());

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        }

        @Override
        protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException failed) throws IOException, ServletException {
                Map<String, Error> mapError = UserAdvice.GenerateUserAdvice(401, "Username or password incorrect.");
                response.setContentType("application/json");
                response.setStatus(401);
                new ObjectMapper().writeValue(response.getOutputStream(), mapError);
        }
}