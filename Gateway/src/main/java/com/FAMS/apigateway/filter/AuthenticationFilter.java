package com.FAMS.apigateway.filter;

import com.FAMS.apigateway.utils.JwtUtil;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JwtUtil util;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (HttpMethod.OPTIONS.matches(exchange.getRequest().getMethod().name())) {
//                exchange.getResponse().getHeaders().set("Access-Control-Allow-Origin", "*");
//                exchange.getResponse().getHeaders().set("Access-Control-Allow-Methods", "*");
//                exchange.getResponse().getHeaders().set("Access-Control-Allow-Headers", "*");
//                exchange.getResponse().getHeaders().set("Access-Control-Allow-Credentials", "*");
                exchange.getResponse().getHeaders().setAccessControlAllowOrigin("*");
                exchange.getResponse().getHeaders().setAccessControlAllowMethods(Arrays.asList(
                        HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS
                ));
                exchange.getResponse().getHeaders().setAccessControlAllowHeaders(List.of("*"));
                exchange.getResponse().getHeaders().setAccessControlAllowCredentials(true);
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }

            ServerHttpRequest newRequest = null;
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                //header contain token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing Authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader!= null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                }
                if(!redisTemplate.hasKey(authHeader)){
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                try{
                    String username = util.validateToken(authHeader);
                    ServerHttpRequest request = exchange.getRequest();
                    addOriginalRequestUrl(exchange, request.getURI());
                    String path = request.getURI().getRawPath();
                    String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(path, "/"))
                            .skip(0).collect(Collectors.joining("/"));
                    newPath += (newPath.length() > 1 && path.endsWith("/") ? "/" : "");
                    newRequest = exchange.getRequest().mutate()
                            .header("username", username)
                            .path(newPath)
                            .build();
                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());
                }catch (Exception e){
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange.mutate().request(newRequest).build());
        });
    }

    public static class Config {

    }
}
