package com.nhnacademy.booklay.booklaygateway.filter;

import com.nhnacademy.booklay.booklaygateway.util.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment env;

    private final TokenUtils tokenUtils;


    public AuthorizationHeaderFilter(Environment env, TokenUtils tokenUtils) {
        super(Config.class);
        this.env = env;
        this.tokenUtils = tokenUtils;
    }

    public static class Config {

    }

    // login -> token -> users (with token) -> header(include token)
    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                log.info("Authorization header is missing");

                return chain.filter(exchange);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            String role = tokenUtils.getRole(jwt);

            exchange.getRequest()
                .mutate()
                .header("WWW-Authenticate", role)
                .build();

            return chain.filter(exchange);
        }));
    }

    private boolean isJwtValid(String jwt) {

        boolean returnVal = true;

        String role = null;
        String email = null;

        try {
            Claims claims = tokenUtils.getClaims(jwt);

            role = String.valueOf(claims.get("role"));
            email = String.valueOf(claims.get("email"));

        } catch (Exception e) {
            returnVal = false;
        }

        if (role == null || role.isEmpty()) {
            returnVal = false;
        }

        log.info("Valid JWT Token username = {}, role = {}", email, role);

        return returnVal;

    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }
}
