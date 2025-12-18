package com.rslakra.ratelimiter.slidingwindowcounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RateLimiterApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RateLimiterApplication.class, args);
    }

}
