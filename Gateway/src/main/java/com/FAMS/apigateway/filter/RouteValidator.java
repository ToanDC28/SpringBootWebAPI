package com.FAMS.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoint = List.of(
            "/user-service/api/user/auth/login",
            "/user-service/api/user/mail/send",
            "/user-service/api/user/mail/validation",
            "/syllabus-service/training-material/download-materials",
            "/eureka"

    );

    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> openApiEndpoint.stream()
                    .noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}
