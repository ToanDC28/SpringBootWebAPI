package com.example.ClassRepo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable().cors().and()
                .authorizeHttpRequests()
                .antMatchers("/class-management/api/class/admin/list").hasAuthority("ADMIN")
                .antMatchers("/class-management/api/class/slots/**").permitAll()
                .antMatchers("/class-management/api/class/**").permitAll()
                .antMatchers("/class-management/api/class/listAll").permitAll()
                .antMatchers("/class-management/api/class/class-detail").permitAll()
                .antMatchers("/class-management/api/class/admin/**")
                .hasAnyAuthority("SUPER_ADMIN","ADMIN")
                .and().build();
    }
}
