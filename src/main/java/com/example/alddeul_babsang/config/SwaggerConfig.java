package com.example.alddeul_babsang.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                .title("알뜰 밥상 in 서울")
                .description("알뜰 밥상 in 서울 api 명세서입니다.<br>" +
                        "<b>오류 및 문의사항은</b> <a> https://github.com/DACOS-SOLUX-Hackathon-Team5/Alddeul_Babsang-Server</a>에" +
                        " <b>\"[bug]~~\" 이름</b>으로 이슈 남겨주세요. ")
                .version("1.0.0");
    }
}
