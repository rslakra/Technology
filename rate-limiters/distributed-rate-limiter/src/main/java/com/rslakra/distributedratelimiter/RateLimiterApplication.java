package com.rslakra.distributedratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.rslakra.distributedratelimiter",
    "com.rslakra.ratelimiters"
})
public class RateLimiterApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RateLimiterApplication.class, args);
    }

}
