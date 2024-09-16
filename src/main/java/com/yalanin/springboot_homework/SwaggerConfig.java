package com.yalanin.springboot_homework;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition
public class SwaggerConfig {

    @Bean
    public OpenAPI springHomeworkOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpringBoot 作業 API 文件")
                        .description("SpringBoot 作業 API 文件")
                        .version("v1"));
    }
}
