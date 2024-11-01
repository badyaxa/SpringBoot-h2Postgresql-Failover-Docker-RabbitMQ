package com.pr.tm.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiDocumentationConfiguration {

    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("Some description for the Task Manager API documentation")
                        .version("0.0.1")
                        .contact(new Contact()
                                .name("Contact Name")
                                .email("email@mail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentation description")
                        .url("https:/wiki.domain.com"));
    }
}
