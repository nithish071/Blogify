package com.nithish.blog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/**").build();
    }

    @Bean
    OpenAPI customOpenAPI(){
        String schemeName="bearerAuth";
        return new OpenAPI()
                .info(new Info().title("Blogging Application").version("1.0").description("Made with love "+ Character.toString(0x1F49C) +" by nithish").contact(new Contact().url("https://www.linkedin.com/in/nithish-kumarp/")))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(
                        new Components().addSecuritySchemes(schemeName,new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                );
    }
}
