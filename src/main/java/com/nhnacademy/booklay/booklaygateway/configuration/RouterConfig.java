package com.nhnacademy.booklay.booklaygateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("get_coupon", r -> r.path("/coupon/**")
                .uri("http://133.186.228.19:8090"))
            .route("get_shop", r -> r.path("/shop/**")
                .uri("http://125.6.38.201:8090"))
            .build();
    }
}