package com.example.UserRepo.config;

import com.example.UserRepo.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
//    private final JwtAuthenticationFilter jwtAuthFilter;
//    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable().cors().and()
                .authorizeHttpRequests()
                .antMatchers("/user-service/api/user/auth/register")
                .hasAnyAuthority("SUPER_ADMIN","ADMIN")
                .antMatchers("/user-service/api/user/auth/login")
                .permitAll()
                .antMatchers("/user-service/api/user/auth/logout").permitAll()
                .antMatchers("/user-service/api/user/auth/refresh").permitAll()
                .antMatchers("/user-service/api/user/admin/**")
                .hasAnyAuthority("SUPER_ADMIN","ADMIN")
                .antMatchers("/user-service/api/user/updateUser").permitAll()
                .antMatchers("/user-service/api/user/getUser").permitAll()
                .antMatchers("/user-service/api/user/upload-profile-image/**").permitAll()
                .antMatchers("/user-service/api/user/mail/**").permitAll()
                .and().build();
    }

}
