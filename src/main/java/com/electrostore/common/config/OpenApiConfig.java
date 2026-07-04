package com.electrostore.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình springdoc-openapi. Swagger UI: /swagger-ui.html (bật/tắt theo profile trong
 * application-*.yml).
 *
 * <p>Khi module có controller thật, thêm GroupedOpenApi để nhóm docs theo module, vd: {@code
 * GroupedOpenApi.builder().group("catalog").pathsToMatch("/api/v1/products/**").build()}.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI electroStoreOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("ElectroStore API")
                        .version("v1")
                        .description("REST API cho hệ thống ecommerce đồ điện tử. "
                                + "Base path: /api/v1. Envelope: docs/main/api-conventions.md."));
    }
}
