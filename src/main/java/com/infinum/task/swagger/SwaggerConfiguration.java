package com.infinum.task.swagger;

import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .forCodeGeneration(false)
        .select()
        .apis(RequestHandlerSelectors.basePackage("hr.infinum.task.controller"))
        .paths(PathSelectors.any())
        .paths(PathSelectors.regex("/api.*"))
        .build()
        .useDefaultResponseMessages(false)
        .consumes(Set.of(MediaType.APPLICATION_JSON_VALUE))
        .apiInfo(new ApiInfoBuilder()
            .version("1.0")
            .title("Infinum task REST API")
            .description("Defines REST API, provided by Infinum, for managing users and their favourite cities")
            .contact(new Contact("Marko Majcenić",
                "https://www.linkedin.com/in/marko-majceni%C4%87-913b73150/",
                "marko.majcenic@gmail.com"))
            .build());
  }

}
