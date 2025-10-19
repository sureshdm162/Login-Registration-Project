package com.credit.userms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
   @Bean
    public OpenAPI customOpenApiSwagger(){
      OpenAPI openApi=new OpenAPI();
       Info info=new Info();
       info.title("User Service APIs");
       info.description("This document provides info about all APIs in the User-Service microservice from Credit-Score-Analysis project.");
      OpenAPI openApi2=openApi.info(info);
      return openApi2;
   }
}