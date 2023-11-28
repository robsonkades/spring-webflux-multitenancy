package app.lumini.api.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi usersOpenApi() {
        String[] paths = { "/api/users/**" };
        return GroupedOpenApi.builder().group("users")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Users API").version("1.0.0")))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi companyOpenApi() {
        String[] paths = { "/api/trails/**" };
        return GroupedOpenApi.builder().group("trails")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Trails API").version("1.0.0")))
                .pathsToMatch(paths)
                .build();
    }
}
