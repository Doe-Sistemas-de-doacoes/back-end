package com.labes.doe.configuration;

import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.labes.doe.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(getApiInfo())
                .securitySchemes( List.of( new ApiKey("JWT","Authorization","header" )) );
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Projeto desenvolvido na disciplina de Laboratório de Engenharia de Software")
                .description("Projeto de doações - Doe")
                .version("1.0")
                .contact(getContact())
                .build();
    }

    private Contact getContact() {
        return new Contact("David Chaves Ferreira", "https://github.com/DavidChavess", "davi.ch.fe@gmail.com");
    }
}