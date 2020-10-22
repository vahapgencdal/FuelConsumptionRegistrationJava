package com.fuel.consumption.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Vahap Gencdal
 * @email avahap19@gmail.com
 */

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    public static final Contact DEFAULT_CONTACT = new Contact(
            "Abdulvahap Gencdal", "/info", "abdulvahapgencdal@gmail.com");

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "Fuel Consumption Registration API", "Fuel consumption management application API for small transportation company", "V1.0",
            "Terms of service", DEFAULT_CONTACT,
            "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Arrays.asList());

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<>(Arrays.asList("application/json"));


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.fuel.consumption.api.v1"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }
}
