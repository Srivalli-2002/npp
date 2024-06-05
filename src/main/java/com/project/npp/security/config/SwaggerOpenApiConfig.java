package com.project.npp.security.config;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
 
@Configuration
//@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SwaggerOpenApiConfig {
	@Bean
	public OpenAPI springOpenAPI() {
		@SuppressWarnings("unused")
		final String securitySchemeName = "bearerAuth";
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("BearerAuthentication",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
				.security(List.of(new SecurityRequirement().addList("BearerAuthentication")))
				.info(new Info().title("SpringBoot API").description("Spring JWT Security application")
						.version("v0.0.1").license(new License().name("Apache 2.0").url("http://springdoc.org")))
				.externalDocs(new ExternalDocumentation().description("SpringBoot Wiki Documentation")
						.url("https://springboot.wiki.github.org/docs"));
 
	}
 
}