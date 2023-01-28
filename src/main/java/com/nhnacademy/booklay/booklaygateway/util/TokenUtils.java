package com.nhnacademy.booklay.booklaygateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenUtils {

    private final JwtParser parser;

    public Claims getClaims(String jwt) {
        return parser.parseClaimsJws(jwt).getBody();
    }

    public String getRole(String jwt) {
        Claims claims = getClaims(jwt);

        return String.valueOf(claims.get("role"));
    }
}
