package com.bbidoleMarket.bbidoleMarket.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

//    @Value("${jwt.access.header}")
//    private String accessTokenHeader;

    @Bean
    public OpenAPI openAPI() {
        // Access Token Bearer 인증 스키마 설정
//        SecurityScheme accessTokenScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER)
//                .name(accessTokenHeader);

        // SecurityRequirement 설정 - 각 토큰별 인증 요구사항 추가
//        SecurityRequirement accessTokenRequirement = new SecurityRequirement().addList(accessTokenHeader);

        Server server = new Server();
        server.setUrl("http://localhost:8080");

        return new OpenAPI()
                .info(new Info()
                        .title("Bbidole")
                        .description("Bbidole REST API Document")
                        .version("1.0.0"));
//                .components(new Components()
//                        .addSecuritySchemes(accessTokenHeader, accessTokenScheme)
//                .addServersItem(server)
//                .addSecurityItem(accessTokenRequirement);
    }

}
