package com.apollo.scentraapi.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Scentra API Test") // API의 제목
                .description("생성형 AI 활용 향수 플랫폼 Scnetra API 테스트 페이지입니다.") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}
