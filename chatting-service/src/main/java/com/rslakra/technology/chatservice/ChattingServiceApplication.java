package com.rslakra.technology.chatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main application class for the Chatting Service application.
 * This class serves as the entry point for the Spring Boot application
 * and is also responsible for configuring the application during deployment
 * when run in a servlet container.
 */
@SpringBootApplication
public class ChattingServiceApplication extends SpringBootServletInitializer {


    /**
     * Configures the application when it's launched by a servlet container.
     *
     * @param builder the SpringApplicationBuilder used to configure the application
     * @return the configured SpringApplicationBuilder instance
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ChattingServiceApplication.class);
    }

    /**
     * The entry point of the ChattingServiceApplication that launches the Spring Boot application.
     *
     * @param args an array of command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ChattingServiceApplication.class, args);
    }
}
