package com.team2.trainingprogramrepo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class TrainingProgramConfig {

    @Value("${class-service-url}")
    private String classServiceUrl;

    @Value("${syllabus-service-url}")
    private String syllabusServiceUrl;

    @Value("${date-format}")
    private String dateFormat;
    @Value("${user-service-url}")
    private String user_url;
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Properties properties() {
        Properties properties = new Properties();

        try {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yml");
            properties.load(inputStream);
        } catch (IOException e) {

            e.printStackTrace();
        }


        if (classServiceUrl != null) {
            properties.setProperty("class-service-url", classServiceUrl);
        }

        if (syllabusServiceUrl != null) {
            properties.setProperty("syllabus-service-url", syllabusServiceUrl);
        }

        if (dateFormat != null) {
            properties.setProperty("date-format", dateFormat);
        }
        properties.setProperty("user-url", user_url);


        return properties;
    }
}
