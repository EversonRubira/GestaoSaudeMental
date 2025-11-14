package gestaoSaudeMental.api.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira o token JWT obtido no endpoint /login")
                        )
                )
                .info(new Info()
                        .title("API Gestão de Saúde Mental")
                        .description("API RESTful para gerenciamento de saúde mental, permitindo registro de estados emocionais, " +
                                "atividades realizadas e acompanhamento do histórico emocional dos usuários. " +
                                "A API utiliza autenticação JWT para proteger os endpoints sensíveis.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("contato@gestaosaudemental.com")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                );
    }
}
