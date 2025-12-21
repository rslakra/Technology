package com.rslaka.swaggersample.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /**
     * @return
     */
    @Bean
    public OpenAPI apiOpenAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    /**
     * @return
     */
    private Info apiInfo() {
        return new Info()
                .title("Swagger API")
                .description("Swagger API reference for developers")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Rohtash")
                        .url("www.rslakra.com")
                        .email("work.lakra@gmail.com"))
                .license(new License()
                        .name("Swagger License")
                        .url("work.lakra@gmail.com"))
                .termsOfService("https://swagger.io/");
    }

}
