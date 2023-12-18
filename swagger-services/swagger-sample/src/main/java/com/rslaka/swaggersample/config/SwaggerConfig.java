package com.rslaka.swaggersample.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
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
    public Docket postsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
            .apiInfo(apiInfo()).select().paths(postPaths()).build();
    }

    /**
     * @return
     */
    private Predicate<String> postPaths() {
        return or(regex("/api/posts.*"), regex("/api/swagger.*"));
    }

    /**
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Swagger API")
            .description("Swagger API reference for developers")
            .termsOfServiceUrl("http://swagger.com")
            .contact("oauth@gmail.com").license("Swagger License")
            .licenseUrl("oauth@gmail.com").version("1.0").build();
    }

}
