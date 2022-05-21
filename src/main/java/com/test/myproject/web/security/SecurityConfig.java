package com.test.myproject.web.security;

import com.test.myproject.core.interfaces.IUserService;
import com.test.myproject.infrastructure.interfaces.IWeatherMapRepository;
import com.test.myproject.web.filters.CustomAuthenticationFilter;
import com.test.myproject.web.filters.CustomAuthorizationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
        private final UserDetailsService userDetailsService;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;
        private final IUserService userService;
        private final IWeatherMapRepository weatherMapRepository;

        @Override
        protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

                authenticationManagerBuilder.userDetailsService(userDetailsService)
                                .passwordEncoder(bCryptPasswordEncoder);
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.cors().and();

                CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
                                authenticationManagerBean());
                customAuthenticationFilter.setFilterProcessesUrl("/api/login");

                httpSecurity.csrf().disable();
                httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                httpSecurity.authorizeRequests()
                                .antMatchers("/api/login/**", "/api/register/**", "/api/token/refresh/**",
                                                "/health/**")
                                .permitAll();

                httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET,
                                "/api/**").hasAnyAuthority("ROLE_USER");
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST,
                                "/api/**").hasAnyAuthority("ROLE_USER");
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.PUT,
                                "/api/**").hasAnyAuthority("ROLE_USER");
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.DELETE,
                                "/api/**").hasAnyAuthority("ROLE_USER");
                httpSecurity.authorizeRequests().antMatchers(HttpMethod.PATCH,
                                "/api/**").hasAnyAuthority("ROLE_USER");

                httpSecurity.authorizeRequests().anyRequest().authenticated();
                httpSecurity.addFilter(customAuthenticationFilter);
                httpSecurity.addFilterBefore(new CustomAuthorizationFilter(userService, weatherMapRepository),
                                UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
                return super.authenticationManagerBean();
        }
}
