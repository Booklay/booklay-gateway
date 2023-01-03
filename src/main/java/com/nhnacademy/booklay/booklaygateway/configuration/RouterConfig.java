package com.nhnacademy.booklay.booklaygateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RouterConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("get_something", r -> r.path("/coupon/**")
                        .uri("https://comsun.shop"))
                .route("get_route", r -> r.path("/member/**")   // localhost:8080/member
                        .uri("https://naver.com"))                      // naver.com/member/

                .build();
    }
}