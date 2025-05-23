package com.team.teamreadioserver.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* 설명. Swagger는 OpenAPI Specification(OAS)이다.
 *  build.gradle에 의존성을 추가해줘야 한다.
 *  접속: http://localhost:8080/swagger-ui/index.html (application.yaml 참고)
 * */
@OpenAPIDefinition(
		info = @Info(title = "readio",
					 description = "React부터 Spring Data Jpa까지 진행하는 서비스 API 명세서",
					 version = "v1"))
@Configuration
public class SwaggerConfig {

	//	@Bean
//	public GroupedOpenApi chatOpenApi() {
//		/* 설명. Swagger에서 처리하고자 하는 경로를 지정 */
//		String [] paths = {"/api/v1/**", "/auth/**"};
//
//		return GroupedOpenApi.builder()
//							 .group("readio 서비스 API v1")
//							 .pathsToMatch(paths)
//							 .build();
//	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:5173")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowCredentials(true);
			}
		};
	}
}








