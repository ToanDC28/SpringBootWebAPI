package com.fams.syllabus_repository.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                //.antMatchers("/syllabus-management/api/syllabus/admin/list").permitAll()
//                .antMatchers("/syllabus-management/api/class/slots/**").permitAll()
//                .antMatchers("/syllabus-management/api/class/**").permitAll()
//                .antMatchers("/syllabus-management/api/class/listAll").permitAll()
//                .antMatchers("/syllabus-management/api/class/class-detail").permitAll()
                .antMatchers("/syllabus-service/api/syllabus/**")
                .hasAnyAuthority("SUPER_ADMIN","ADMIN")
                .and().build();
    }

}
