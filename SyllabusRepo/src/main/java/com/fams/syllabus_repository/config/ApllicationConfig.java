package com.fams.syllabus_repository.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
public class ApllicationConfig {
    @Value("${data.training}")
    private String training_url;
    @Value("${data.class}")
    private String class_url;
    @Value("${data.user}")
    private String user_url;
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Properties properties(){
        Properties properties = new Properties();
        properties.setProperty("training", training_url);
        properties.setProperty("user-url", user_url);
        properties.setProperty("class-url", class_url);
        return properties;
    }
}
