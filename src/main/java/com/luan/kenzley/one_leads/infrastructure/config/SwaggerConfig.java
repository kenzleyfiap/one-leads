package com.luan.kenzley.one_leads.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ONE Leads API",
                description = "API para gerenciamento de leads e contatos empresariais",
                version = "0.0.1-SNAPSHOT",
                contact = @Contact(
                        name = "Luan Kenzley",
                        email = "kenzleydev@outlook.com"
                )

        )
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi customImplementation() {
        return GroupedOpenApi.builder()
                .group("one-leads")
                .packagesToScan("com.luan.kenzley.one_leads.controller")
                .build();
    }
}
