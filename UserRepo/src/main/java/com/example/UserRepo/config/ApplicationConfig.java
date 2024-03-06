package com.example.UserRepo.config;

import com.example.UserRepo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Value("${format.date}")
    private String date_format;

    @Value("${format.avatar}")
    private String avatar_format;

    @Value("${format.avatar-address}")
    private String avatar_address;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${format.email}")
    private String emailFormat;

    @Value("${format.password}")
    private String passwordFormat;
    @Value("${format.training}")
    private String training_url;
    @Value("${format.class}")
    private String class_url;

    @Value("${format.permission}")
    private String redis_permission;

    @Value("${format.user}")
    private String redis_user;

    private final UserRepository repository;
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public Properties properties() {
        Properties properties = new Properties();
        properties.setProperty("date", date_format);
        properties.setProperty("avatar", avatar_format);
        properties.setProperty("secretKey", secretKey);
        properties.setProperty("email", emailFormat);
        properties.setProperty("pass", passwordFormat);
        properties.setProperty("image_address", avatar_address);
        properties.setProperty("training", training_url);
        properties.setProperty("class-url", class_url);
        properties.setProperty("redis-permission", redis_permission);
        properties.setProperty("redis-user", redis_user);
        return properties;
    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
