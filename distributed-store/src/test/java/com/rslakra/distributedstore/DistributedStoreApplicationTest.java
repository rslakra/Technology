package com.rslakra.distributedstore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@ContextConfiguration
@SpringBootTest(classes = {DistributedStoreApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DistributedStoreApplicationTest {

    @Test
    void contextLoads() {
    }

}
