package com.nhnacademy.booklay.booklaygateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class HeaderFilter extends AbstractGatewayFilterFactory<HeaderFilter.Config> {

    public HeaderFilter() {
        super(Config.class);
    }

    public static class Config {}

    public static final String FORWARDED_FOR = "X-Forwarded-For";

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("===============");

            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();

            List<String> forwardedFor = headers.get(FORWARDED_FOR);

            if (Objects.nonNull(forwardedFor) && Objects.nonNull(forwardedFor.get(0))) {
                log.info("{}: {}", FORWARDED_FOR, forwardedFor.get(0));
                log.info("URI: {}", request.getURI());
            }

            List<String> jwts = headers.get(HttpHeaders.AUTHORIZATION);

            if (Objects.nonNull(jwts) && Objects.nonNull(jwts.get(0))) {
                String jwt = jwts.get(0);

                ServerHttpResponse response = exchange.getResponse();
                response.beforeCommit(() -> {
                    response.getHeaders().set(HttpHeaders.AUTHORIZATION, jwt);
                    return Mono.empty();
                });

            }

            return chain.filter(exchange);
        });
    }

}
