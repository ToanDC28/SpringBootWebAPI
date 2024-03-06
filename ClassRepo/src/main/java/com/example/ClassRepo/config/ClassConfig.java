package com.example.ClassRepo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
public class ClassConfig {

    @Value("${data.format}")
    private String date_format;
    @Value("${data.training}")
    private String training_url;
    @Value("${data.syllabus}")
    private String syllabus_url;
    @Value("${data.user}")
    private String user_url;
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
//        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        clientHttpRequestFactory.setConnectTimeout(3000);
        return new RestTemplate();
    }

    @Bean
    public Properties properties(){
        Properties properties = new Properties();
        properties.setProperty("date", date_format);
        properties.setProperty("training", training_url);
        properties.setProperty("user-url", user_url);
        properties.setProperty("syllabus-service-url", syllabus_url);
        return properties;
    }

}
