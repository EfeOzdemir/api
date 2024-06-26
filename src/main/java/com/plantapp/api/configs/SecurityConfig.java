package com.plantapp.api.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer -> jwtConfigurer.jwkSetUri(
                                       "https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com"
                                )
                        ))
                .authorizeHttpRequests(matcherRegistry ->
                        matcherRegistry
                                .requestMatchers("/community/").permitAll()
                                .requestMatchers(HttpMethod.POST, "/community/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/community/**").authenticated()
                                .anyRequest().permitAll()).build();
    }
}
