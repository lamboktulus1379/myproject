package com.test.myproject.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class WebConfig implements WebMvcConfigurer {
    public String[] allowedOrigins;

    @Value("${cors.allowed.origins}")
    public String allowedOriginsString = "http://localhost:4200/,https://hai-myproject.firebaseapp.com/,https://myproject.gratype.space/";

    public WebConfig() {
        this.allowedOrigins = this.allowedOriginsString.split(",");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] exposedHeaders = { "weather", "fullname" };
        registry.addMapping("/api/**")
                .allowedOrigins(this.allowedOrigins)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .exposedHeaders(exposedHeaders)
                .maxAge(3600);
    }
}