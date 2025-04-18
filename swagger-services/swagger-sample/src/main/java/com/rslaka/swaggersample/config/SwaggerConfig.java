package com.rslaka.swaggersample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("public-api").apiInfo(apiInfo()).select().paths(PathSelectors.any()).build();

    }

    /**
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Swagger API").description("Swagger API reference for developers").termsOfServiceUrl("http://swagger.com").contact(new Contact("Rohtash", "www.rslakra.com", "swagger@gmail.com")).license("Swagger License").licenseUrl("oauth@gmail.com").version("1.0.0").build();
    }


}
