package com.example.UserRepo.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
public class SwaggerConfig {

    @Bean
    public WebClient.Builder getWebClient(){
        return WebClient.builder();
    }

    @Bean
    public OpenAPI openAPI(){
        Info info = new Info().description("Api FaMS").title("Team_2").version("V1");
        return new OpenAPI().info(info);
    }

}
