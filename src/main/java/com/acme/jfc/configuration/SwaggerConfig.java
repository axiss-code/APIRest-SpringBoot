package com.acme.jfc.configuration;


import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket apiDocket(){
        return new Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.acme.jfc.controller"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(new ApiInfo("ACME ApiRest  v1", 
                    "Permite la gestion de Prendas y Promociones",
                    "1.0.0", "Free to use", 
                    new Contact("Jorge F.", "https://www.hiberus.com/", "jorge@email.com"),
                    "License", "http://gpl.com/licence", Collections.emptyList()));
     
    }
    
}
