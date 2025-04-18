package com.rslakra.accountservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

//@ContextConfiguration
@SpringBootTest(classes = {AccountServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AccountServiceApplicationTest {

    @Test
    void contextLoads() {
    }

}
