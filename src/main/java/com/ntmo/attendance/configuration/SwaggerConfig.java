package com.ntmo.attendance.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	  @Bean
	  public GroupedOpenApi publicApi() {
	      return GroupedOpenApi.builder()
	              .group("attendance")
	              .packagesToScan("com.ntmo.attendance.controller")
	              .pathsToMatch("/**")
	              .build();
	  }
}