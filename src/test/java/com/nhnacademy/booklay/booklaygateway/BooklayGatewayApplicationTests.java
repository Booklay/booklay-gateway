package com.nhnacademy.booklay.booklaygateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("prod")
@SpringBootTest
class BooklayGatewayApplicationTests {

    @Test
    void contextLoads() {

    }

}
