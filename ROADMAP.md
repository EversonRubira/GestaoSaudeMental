# 🗺️ ROADMAP DE MELHORIAS - GESTÃO SAÚDE MENTAL

**Versão Base:** 1.0.0 (Snapshot)
**Data Início:** 17/11/2025
**Objetivo:** Transformar o projeto em uma aplicação de nível produção com arquitetura moderna e escalável

---

## 📋 ÍNDICE

1. [Visão Geral](#visão-geral)
2. [Fases de Implementação](#fases-de-implementação)
3. [Fase 1: Fundação e Refatoração](#fase-1-fundação-e-refatoração)
4. [Fase 2: Segurança Avançada](#fase-2-segurança-avançada)
5. [Fase 3: Documentação e Qualidade](#fase-3-documentação-e-qualidade)
6. [Fase 4: Testes Automatizados](#fase-4-testes-automatizados)
7. [Fase 5: Performance e Escalabilidade](#fase-5-performance-e-escalabilidade)
8. [Fase 6: Analytics e Inteligência](#fase-6-analytics-e-inteligência)
9. [Fase 7: Infraestrutura e DevOps](#fase-7-infraestrutura-e-devops)
10. [Fase 8: Arquitetura Hexagonal](#fase-8-arquitetura-hexagonal)
11. [Fase 9: Recursos Avançados](#fase-9-recursos-avançados)
12. [Cronograma e Estimativas](#cronograma-e-estimativas)
13. [Critérios de Sucesso](#critérios-de-sucesso)

---

## 🎯 VISÃO GERAL

### Objetivo Estratégico

Evoluir o projeto de um MVP funcional para uma aplicação enterprise-ready, demonstrando:
- Arquitetura de software moderna e escalável
- Boas práticas de desenvolvimento
- Segurança robusta
- Qualidade de código com alta cobertura de testes
- Infraestrutura como código
- DevOps e automação

### Prioridades

```
P0 (Crítica)    → Impede uso em produção
P1 (Alta)       → Melhora significativa de qualidade
P2 (Média)      → Adiciona valor importante
P3 (Baixa)      → Nice to have
```

### Tecnologias que Serão Adicionadas

- ✨ **JWT** - Autenticação stateless
- ✨ **Redis** - Cache distribuído
- ✨ **Swagger/OpenAPI** - Documentação interativa
- ✨ **JUnit 5 + Mockito** - Testes robustos
- ✨ **Docker + Docker Compose** - Containerização
- ✨ **Prometheus + Grafana** - Monitoramento
- ✨ **GitHub Actions** - CI/CD
- ✨ **Logback + ELK** - Logging estruturado

---

## 📊 FASES DE IMPLEMENTAÇÃO

```
FASE 1: Fundação              [2 semanas]  P0
   ↓
FASE 2: Segurança             [1 semana]   P0
   ↓
FASE 3: Documentação          [1 semana]   P1
   ↓
FASE 4: Testes                [2 semanas]  P1
   ↓
FASE 5: Performance           [1 semana]   P1
   ↓
FASE 6: Analytics             [2 semanas]  P2
   ↓
FASE 7: Infraestrutura        [2 semanas]  P1
   ↓
FASE 8: Arquitetura           [2 semanas]  P2
   ↓
FASE 9: Recursos Avançados    [3 semanas]  P3

TOTAL: ~16 semanas (4 meses)
```

---

## 🔨 FASE 1: FUNDAÇÃO E REFATORAÇÃO

**Duração:** 2 semanas
**Prioridade:** P0 (Crítica)
**Objetivo:** Estabelecer base sólida e corrigir problemas arquiteturais

### 1.1. Criar Camada de Service (P0)

**Problema Atual:** Lógica de negócio espalhada nos controllers

**Solução:**
- [ ] Criar pacote `service`
- [ ] Implementar `UsuarioService`
- [ ] Implementar `HistoricoEmocionalService`
- [ ] Implementar `AutenticacaoService` (refatorar existente)
- [ ] Mover toda lógica de negócio dos controllers para services
- [ ] Controllers devem ter apenas: validação HTTP, chamada service, retorno HTTP

**Arquivos a criar:**
```
src/main/java/gestaoSaudeMental/api/service/
├── UsuarioService.java
├── HistoricoEmocionalService.java
└── MensagemMotivacionalService.java
```

**Exemplo de refatoração:**
```java
// ANTES: UsuarioController
@PostMapping
@Transactional
public ResponseEntity<DadosUsuarioCriadoDTO> cadastrar(...) {
    Usuario usuario = new Usuario(dados);
    repository.save(usuario);
    String senhaHash = passwordEncoder.encode(dados.senha());
    // ... mais lógica
}

// DEPOIS: UsuarioController
@PostMapping
public ResponseEntity<DadosUsuarioCriadoDTO> cadastrar(...) {
    DadosUsuarioCriadoDTO resultado = usuarioService.cadastrarUsuario(dados);
    return ResponseEntity.created(...).body(resultado);
}

// DEPOIS: UsuarioService
@Service
public class UsuarioService {
    @Transactional
    public DadosUsuarioCriadoDTO cadastrarUsuario(DadosCadastroUsuario dados) {
        // toda a lógica aqui
    }
}
```

**Critério de Sucesso:**
- ✅ Controllers com no máximo 10 linhas por método
- ✅ Lógica de negócio 100% nos services
- ✅ Todos os métodos transacionais nos services

---

### 1.2. Global Exception Handler (P0)

**Problema Atual:** Exceções genéricas e sem padronização

**Solução:**
- [ ] Criar pacote `exception`
- [ ] Criar custom exceptions:
  - `UsuarioNaoEncontradoException`
  - `EmailJaExisteException`
  - `HistoricoNaoEncontradoException`
  - `DadosInvalidosException`
- [ ] Implementar `@RestControllerAdvice`
- [ ] Criar DTOs de erro padronizados
- [ ] Tratar todas as exceções conhecidas

**Arquivos a criar:**
```
src/main/java/gestaoSaudeMental/api/exception/
├── UsuarioNaoEncontradoException.java
├── EmailJaExisteException.java
├── HistoricoNaoEncontradoException.java
├── GlobalExceptionHandler.java
└── dto/
    ├── ErroDTO.java
    └── ErroValidacaoDTO.java
```

**Respostas de erro padronizadas:**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 404,
  "error": "Não Encontrado",
  "mensagem": "Usuário com ID 999 não encontrado",
  "path": "/usuarios/999"
}
```

**Critério de Sucesso:**
- ✅ Zero `RuntimeException` genéricas
- ✅ Todas as exceções têm tratamento específico
- ✅ Respostas de erro seguem padrão RFC 7807

---

### 1.3. Logging Estruturado (P0)

**Problema Atual:** Sem logs, debugging difícil

**Solução:**
- [ ] Configurar Logback
- [ ] Adicionar SLF4J em todos os services
- [ ] Implementar logs em níveis apropriados:
  - `ERROR`: Erros críticos
  - `WARN`: Situações anormais
  - `INFO`: Operações importantes
  - `DEBUG`: Detalhes técnicos
- [ ] Logs estruturados em JSON (Logstash encoder)
- [ ] Rotação de logs

**Dependência a adicionar:**
```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>
```

**Arquivo a criar:**
```
src/main/resources/logback-spring.xml
```

**Exemplo de uso:**
```java
@Service
@Slf4j
public class UsuarioService {
    public DadosUsuarioCriadoDTO cadastrarUsuario(DadosCadastroUsuario dados) {
        log.info("Iniciando cadastro de usuário: email={}", dados.email());
        try {
            // lógica
            log.info("Usuário cadastrado com sucesso: id={}", usuario.getId());
        } catch (Exception e) {
            log.error("Erro ao cadastrar usuário: email={}", dados.email(), e);
            throw e;
        }
    }
}
```

**Critério de Sucesso:**
- ✅ Todos os services têm logs
- ✅ Operações importantes são logadas
- ✅ Erros contêm stack trace
- ✅ Logs em formato JSON

---

### 1.4. Externalizar Configurações (P1)

**Problema Atual:** Credenciais hardcoded

**Solução:**
- [ ] Usar variáveis de ambiente
- [ ] Criar `application-dev.properties`
- [ ] Criar `application-prod.properties`
- [ ] Documentar variáveis necessárias

**Refatoração:**
```properties
# ANTES
spring.datasource.password=root

# DEPOIS
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/gestaosaudemental_api}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
```

**Arquivo a criar:**
```
.env.example (não commitar .env real)
```

**Critério de Sucesso:**
- ✅ Zero credenciais no código
- ✅ Configurações por ambiente
- ✅ .env.example documentado

---

### 1.5. Melhorar Validações (P2)

**Solução:**
- [ ] Adicionar validação de força de senha
- [ ] Validar unicidade de email antes de salvar
- [ ] Adicionar validações customizadas
- [ ] Mensagens de erro em português

**Implementar:**
```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SenhaForteValidator.class)
public @interface SenhaForte {
    String message() default "Senha deve ter pelo menos 8 caracteres, letras e números";
}
```

**Critério de Sucesso:**
- ✅ Senhas fracas rejeitadas
- ✅ Email único validado
- ✅ Mensagens de erro claras

---

## 🔐 FASE 2: SEGURANÇA AVANÇADA

**Duração:** 1 semana
**Prioridade:** P0 (Crítica)
**Objetivo:** Implementar segurança de nível produção

### 2.1. Implementar JWT (P0)

**Problema Atual:** Sessão stateful, não escalável

**Solução:**
- [ ] Adicionar dependência `java-jwt`
- [ ] Criar `TokenService`
- [ ] Implementar `SecurityFilter`
- [ ] Atualizar `SecurityConfiguration`
- [ ] Endpoint de login retorna JWT
- [ ] Todos os endpoints protegidos validam JWT

**Dependência:**
```xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.4.0</version>
</dependency>
```

**Arquivos a criar:**
```
src/main/java/gestaoSaudeMental/api/infra/security/
├── TokenService.java
└── SecurityFilter.java
```

**DTO a criar:**
```java
public record DadosTokenJWT(
    String token,
    Long usuarioId,
    String nome,
    LocalDateTime expiresAt
) {}
```

**Critério de Sucesso:**
- ✅ Login retorna JWT
- ✅ JWT expira em 2 horas
- ✅ Endpoints protegidos exigem Bearer token
- ✅ Token inválido retorna 401

---

### 2.2. Configurar CORS (P0)

**Problema Atual:** Sem configuração CORS

**Solução:**
- [ ] Criar `CorsConfig`
- [ ] Permitir origins específicas
- [ ] Configurar headers e métodos
- [ ] Ambiente específico (dev vs prod)

**Arquivo a criar:**
```
src/main/java/gestaoSaudeMental/api/infra/security/CorsConfig.java
```

**Critério de Sucesso:**
- ✅ Frontend específico pode acessar
- ✅ Outros origins bloqueados
- ✅ Preflight requests funcionam

---

### 2.3. Rate Limiting (P1)

**Problema Atual:** Vulnerável a força bruta

**Solução:**
- [ ] Adicionar Bucket4j
- [ ] Implementar `RateLimitFilter`
- [ ] Limitar por IP
- [ ] Limitar endpoint de login (10 req/min)

**Dependência:**
```xml
<dependency>
    <groupId>com.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>8.7.0</version>
</dependency>
```

**Critério de Sucesso:**
- ✅ Login limitado a 10 tentativas/minuto
- ✅ Retorna 429 Too Many Requests
- ✅ Header com tempo de retry

---

### 2.4. Auditoria de Entidades (P2)

**Solução:**
- [ ] Criar classe `Auditavel`
- [ ] Adicionar campos: `criadoEm`, `atualizadoEm`, `criadoPor`, `atualizadoPor`
- [ ] Configurar `@EnableJpaAuditing`
- [ ] Herdar em `Usuario` e `HistoricoEmocional`

**Critério de Sucesso:**
- ✅ Toda alteração registra quem e quando
- ✅ Automático via JPA

---

### 2.5. HTTPS Enforced (P1)

**Solução:**
- [ ] Configurar TLS em produção
- [ ] Redirecionar HTTP → HTTPS
- [ ] Certificado SSL (Let's Encrypt)

**Critério de Sucesso:**
- ✅ Apenas HTTPS em produção
- ✅ HTTP redireciona

---

## 📚 FASE 3: DOCUMENTAÇÃO E QUALIDADE

**Duração:** 1 semana
**Prioridade:** P1 (Alta)
**Objetivo:** Documentação completa e profissional

### 3.1. Implementar Swagger/OpenAPI (P1)

**Solução:**
- [ ] Adicionar SpringDoc OpenAPI
- [ ] Configurar `OpenApiConfig`
- [ ] Documentar todos os endpoints
- [ ] Adicionar exemplos de request/response
- [ ] Documentar schemas
- [ ] Adicionar autenticação JWT no Swagger UI

**Dependência:**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

**Anotações a adicionar:**
```java
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário no sistema")
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
})
```

**URLs:**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

**Critério de Sucesso:**
- ✅ Todos os endpoints documentados
- ✅ Exemplos funcionais
- ✅ Possível testar via Swagger UI

---

### 3.2. JavaDoc Completo (P2)

**Solução:**
- [ ] Adicionar JavaDoc em todas as classes públicas
- [ ] Documentar parâmetros e retornos
- [ ] Gerar documentação HTML

**Exemplo:**
```java
/**
 * Serviço responsável pelo gerenciamento de usuários.
 *
 * <p>Fornece operações de CRUD e lógica de negócio relacionada
 * a usuários, incluindo cadastro, autenticação e histórico emocional.</p>
 *
 * @author Seu Nome
 * @since 1.0.0
 */
@Service
public class UsuarioService {

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param dados dados do usuário a ser cadastrado
     * @return dados do usuário criado (id e nome)
     * @throws EmailJaExisteException se o email já estiver cadastrado
     */
    public DadosUsuarioCriadoDTO cadastrarUsuario(DadosCadastroUsuario dados) {
        // ...
    }
}
```

**Critério de Sucesso:**
- ✅ Classes públicas documentadas
- ✅ Métodos públicos documentados
- ✅ HTML gerado com `mvn javadoc:javadoc`

---

### 3.3. Atualizar README (P1)

**Solução:**
- [ ] Adicionar badges (build status, coverage, etc)
- [ ] Melhorar seção de instalação
- [ ] Adicionar screenshots/GIFs
- [ ] Documentar todas as variáveis de ambiente
- [ ] Adicionar seção de contribuição
- [ ] Troubleshooting comum

**Badges a adicionar:**
```markdown
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen)
![Build](https://github.com/user/repo/workflows/CI/badge.svg)
![Coverage](https://img.shields.io/codecov/c/github/user/repo)
```

**Critério de Sucesso:**
- ✅ README profissional
- ✅ Instruções claras
- ✅ Visual atraente

---

### 3.4. Criar Diagramas (P3)

**Solução:**
- [ ] Diagrama de arquitetura
- [ ] Diagrama de classes
- [ ] Diagrama de sequência (login, cadastro)
- [ ] ER Diagram

**Ferramentas:**
- PlantUML
- Draw.io
- Mermaid

**Critério de Sucesso:**
- ✅ Diagramas no README
- ✅ Código PlantUML versionado

---

## 🧪 FASE 4: TESTES AUTOMATIZADOS

**Duração:** 2 semanas
**Prioridade:** P1 (Alta)
**Objetivo:** Cobertura de testes > 80%

### 4.1. Testes Unitários - Services (P1)

**Solução:**
- [ ] Testar `UsuarioService`
- [ ] Testar `HistoricoEmocionalService`
- [ ] Testar `MensagemMotivacionalService`
- [ ] Usar Mockito para mocks
- [ ] Cobertura > 90% nos services

**Estrutura:**
```
src/test/java/gestaoSaudeMental/api/service/
├── UsuarioServiceTest.java
├── HistoricoEmocionalServiceTest.java
└── MensagemMotivacionalServiceTest.java
```

**Exemplo:**
```java
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private CredenciaisRepository credenciaisRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        // Arrange
        DadosCadastroUsuario dados = criarDadosValidos();
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);

        // Act
        DadosUsuarioCriadoDTO resultado = usuarioService.cadastrarUsuario(dados);

        // Assert
        assertThat(resultado).isNotNull();
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.cadastrarUsuario(criarDadosValidos()))
            .isInstanceOf(EmailJaExisteException.class);
    }
}
```

**Critério de Sucesso:**
- ✅ Cobertura > 90% nos services
- ✅ Testes para casos de sucesso
- ✅ Testes para casos de erro
- ✅ Nomenclatura descritiva

---

### 4.2. Testes de Integração - Controllers (P1)

**Solução:**
- [ ] Testar todos os endpoints
- [ ] Usar `@SpringBootTest` e `MockMvc`
- [ ] Testar validações
- [ ] Testar segurança (JWT)
- [ ] Usar banco H2 em memória

**Estrutura:**
```
src/test/java/gestaoSaudeMental/api/controller/
├── UsuarioControllerIntegrationTest.java
├── AutenticacaoControllerIntegrationTest.java
└── HistoricoEmocionalControllerIntegrationTest.java
```

**Exemplo:**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /usuarios - Deve cadastrar usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() throws Exception {
        DadosCadastroUsuario dados = criarDadosValidos();
        String json = objectMapper.writeValueAsString(dados);

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nome").value(dados.nome()));
    }

    @Test
    @DisplayName("POST /usuarios - Deve retornar 400 quando email inválido")
    void deveRetornar400QuandoEmailInvalido() throws Exception {
        String json = "{\"nome\":\"Teste\",\"email\":\"invalido\"}";

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }
}
```

**Configurar H2:**
```properties
# test/resources/application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.flyway.enabled=false
```

**Critério de Sucesso:**
- ✅ Todos os endpoints testados
- ✅ Casos de sucesso e erro
- ✅ Validações testadas

---

### 4.3. Testes de Repository (P2)

**Solução:**
- [ ] Testar queries customizadas
- [ ] Usar `@DataJpaTest`
- [ ] Testar relacionamentos

**Critério de Sucesso:**
- ✅ Queries customizadas testadas
- ✅ Relacionamentos funcionam

---

### 4.4. Cobertura de Código (P1)

**Solução:**
- [ ] Configurar JaCoCo
- [ ] Gerar relatório de cobertura
- [ ] Meta: > 80%

**Plugin Maven:**
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Comando:**
```bash
mvn clean test jacoco:report
# Ver em: target/site/jacoco/index.html
```

**Critério de Sucesso:**
- ✅ Cobertura total > 80%
- ✅ Services > 90%
- ✅ Controllers > 80%

---

## 🚀 FASE 5: PERFORMANCE E ESCALABILIDADE

**Duração:** 1 semana
**Prioridade:** P1 (Alta)
**Objetivo:** Otimizar performance e preparar para escala

### 5.1. Implementar Cache com Redis (P1)

**Solução:**
- [ ] Adicionar Spring Data Redis
- [ ] Configurar `RedisCacheManager`
- [ ] Cachear usuários
- [ ] Cachear históricos frequentes
- [ ] TTL configurável

**Dependências:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

**Configuração:**
```java
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10));
        return RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }
}
```

**Uso:**
```java
@Cacheable(value = "usuarios", key = "#id")
public Usuario buscarPorId(Long id) { }

@CacheEvict(value = "usuarios", key = "#id")
public void desativar(Long id) { }
```

**Critério de Sucesso:**
- ✅ Usuários cacheados
- ✅ Redução de 50%+ em queries repetidas
- ✅ Cache invalidado corretamente

---

### 5.2. Paginação (P1)

**Solução:**
- [ ] Adicionar paginação em histórico
- [ ] Usar `Pageable` do Spring Data
- [ ] Retornar `Page<T>` nos endpoints

**Exemplo:**
```java
// Repository
Page<HistoricoEmocional> findByUsuarioId(Long usuarioId, Pageable pageable);

// Service
public Page<DadosListagemEstadoEmocionalDTO> listarHistorico(
    Long usuarioId, int pagina, int tamanho) {

    Pageable pageable = PageRequest.of(pagina, tamanho,
        Sort.by("dataRegistro").descending());

    return repository.findByUsuarioId(usuarioId, pageable)
        .map(DadosListagemEstadoEmocionalDTO::new);
}

// Controller
@GetMapping("/{id}/historico")
public ResponseEntity<Page<DadosListagemEstadoEmocionalDTO>> listar(
    @PathVariable Long id,
    @RequestParam(defaultValue = "0") int pagina,
    @RequestParam(defaultValue = "20") int tamanho) {

    return ResponseEntity.ok(service.listarHistorico(id, pagina, tamanho));
}
```

**Critério de Sucesso:**
- ✅ Histórico paginado
- ✅ Parâmetros page e size funcionam
- ✅ Metadata de paginação no response

---

### 5.3. Otimização de Queries (P1)

**Solução:**
- [ ] Identificar N+1 queries
- [ ] Adicionar `JOIN FETCH`
- [ ] Criar índices no banco
- [ ] Usar DTOs em queries

**Exemplo:**
```java
// Antes (N+1)
List<HistoricoEmocional> historicos = repository.findByUsuarioId(id);
// Cada acesso a historico.getUsuario() gera query

// Depois
@Query("SELECT h FROM HistoricoEmocional h JOIN FETCH h.usuario WHERE h.usuario.id = :id")
List<HistoricoEmocional> findByUsuarioIdComUsuario(@Param("id") Long id);
```

**Índices:**
```sql
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_historico_usuario_id ON historico_emocional(usuario_id);
CREATE INDEX idx_historico_data ON historico_emocional(data_registro);
```

**Critério de Sucesso:**
- ✅ Zero N+1 queries
- ✅ Índices criados
- ✅ Queries < 100ms

---

### 5.4. Spring Boot Actuator (P1)

**Solução:**
- [ ] Adicionar Spring Boot Actuator
- [ ] Configurar endpoints de saúde
- [ ] Expor métricas
- [ ] Integrar com Prometheus

**Dependência:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

**Configuração:**
```properties
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
management.metrics.export.prometheus.enabled=true
```

**Endpoints:**
- `/actuator/health` - Status da aplicação
- `/actuator/metrics` - Métricas
- `/actuator/prometheus` - Formato Prometheus

**Critério de Sucesso:**
- ✅ Actuator configurado
- ✅ Health checks funcionam
- ✅ Métricas expostas

---

## 📊 FASE 6: ANALYTICS E INTELIGÊNCIA

**Duração:** 2 semanas
**Prioridade:** P2 (Média)
**Objetivo:** Adicionar inteligência e análises ao sistema

### 6.1. Análise Emocional (P2)

**Solução:**
- [ ] Criar `AnaliseMentalService`
- [ ] Relatório mensal de emoções
- [ ] Distribuição de estados emocionais
- [ ] Emoção dominante
- [ ] Percentual de dias positivos/negativos

**Endpoint:**
```java
GET /usuarios/{id}/relatorio?mes=2025-11
```

**Response:**
```json
{
  "mes": "2025-11",
  "totalRegistros": 30,
  "distribuicaoEmocoes": {
    "FELIZ": 12,
    "TRISTE": 8,
    "ANSIOSO": 6,
    "CANSADO": 4
  },
  "emocaoDominante": "FELIZ",
  "diasPositivos": 12,
  "diasNegativos": 14,
  "percentualPositivo": 40.0
}
```

**Critério de Sucesso:**
- ✅ Relatório mensal funciona
- ✅ Estatísticas corretas
- ✅ Performance < 500ms

---

### 6.2. Tendências e Padrões (P2)

**Solução:**
- [ ] Análise de tendências semanais
- [ ] Identificar padrões recorrentes
- [ ] Correlação emoção x atividade

**Endpoint:**
```java
GET /usuarios/{id}/tendencias?mes=2025-11
```

**Response:**
```json
{
  "tendenciasSemanal": [
    {
      "semana": 1,
      "emocaoDominante": "FELIZ",
      "percentualPositivo": 60.0
    },
    {
      "semana": 2,
      "emocaoDominante": "ANSIOSO",
      "percentualPositivo": 30.0
    }
  ],
  "padroes": [
    {
      "emocao": "FELIZ",
      "atividadesMaisComuns": ["EXERCICIO", "SOCIALIZAR"]
    }
  ]
}
```

**Critério de Sucesso:**
- ✅ Tendências calculadas
- ✅ Padrões identificados

---

### 6.3. Recomendações Personalizadas (P2)

**Solução:**
- [ ] Motor de recomendações
- [ ] Baseado em histórico do usuário
- [ ] Sugestões de atividades
- [ ] Alertas de padrões negativos

**Lógica:**
```java
public List<String> gerarRecomendacoes(Long usuarioId) {
    // Se percentual positivo < 30%
    // → Recomendar buscar ajuda profissional

    // Se emoção dominante = ANSIOSO
    // → Recomendar meditação e exercícios

    // Se tendência decrescente
    // → Alertar sobre padrão negativo
}
```

**Critério de Sucesso:**
- ✅ Recomendações relevantes
- ✅ Baseadas em dados reais

---

### 6.4. Exportação de Dados (P3)

**Solução:**
- [ ] Exportar histórico em PDF
- [ ] Exportar histórico em CSV
- [ ] Gráficos de evolução

**Endpoints:**
```java
GET /usuarios/{id}/exportar/pdf?mes=2025-11
GET /usuarios/{id}/exportar/csv?inicio=2025-01-01&fim=2025-12-31
```

**Dependência:**
```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
</dependency>
```

**Critério de Sucesso:**
- ✅ PDF gerado com sucesso
- ✅ CSV válido

---

## 🐳 FASE 7: INFRAESTRUTURA E DEVOPS

**Duração:** 2 semanas
**Prioridade:** P1 (Alta)
**Objetivo:** Containerização e automação completa

### 7.1. Docker e Docker Compose (P1)

**Solução:**
- [ ] Criar Dockerfile multi-stage
- [ ] Criar docker-compose.yml
- [ ] Serviços: app, mysql, redis, prometheus, grafana
- [ ] Healthchecks
- [ ] Volumes persistentes

**Dockerfile:**
```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /app/target/*.jar app.jar
HEALTHCHECK --interval=30s CMD wget --spider http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**docker-compose.yml:**
```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: gestaosaudemental_api
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping"]
      interval: 10s

  redis:
    image: redis:7-alpine
    volumes:
      - redis_data:/data
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]

  app:
    build: .
    depends_on:
      mysql:
        condition: service_healthy
      redis:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/gestaosaudemental_api
      SPRING_DATA_REDIS_HOST: redis
    ports:
      - "8080:8080"

volumes:
  mysql_data:
  redis_data:
```

**Comandos:**
```bash
# Build e run
docker-compose up --build

# Stop
docker-compose down

# Logs
docker-compose logs -f app
```

**Critério de Sucesso:**
- ✅ Docker build sem erros
- ✅ Todos os serviços sobem
- ✅ Aplicação funciona containerizada

---

### 7.2. CI/CD com GitHub Actions (P1)

**Solução:**
- [ ] Pipeline de build
- [ ] Pipeline de testes
- [ ] Pipeline de deploy
- [ ] Code coverage report
- [ ] Notificações

**Arquivo:**
```yaml
# .github/workflows/ci.yml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven

    - name: Build with Maven
      run: mvn clean install

    - name: Run tests
      run: mvn test

    - name: Generate coverage report
      run: mvn jacoco:report

    - name: Upload coverage to Codecov
      uses: codecov/codecov-action@v3
      with:
        file: ./target/site/jacoco/jacoco.xml

    - name: Build Docker image
      run: docker build -t gestao-saude-mental:${{ github.sha }} .
```

**Critério de Sucesso:**
- ✅ Build automático em push
- ✅ Testes executados
- ✅ Coverage reportado
- ✅ Docker image criada

---

### 7.3. Monitoramento com Prometheus + Grafana (P2)

**Solução:**
- [ ] Configurar Prometheus
- [ ] Configurar Grafana
- [ ] Dashboards customizados
- [ ] Alertas

**prometheus.yml:**
```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'spring-actuator'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['app:8080']
```

**Dashboard Grafana:**
- JVM Memory
- HTTP Requests
- Response Times
- Database Connections
- Cache Hit Rate

**Critério de Sucesso:**
- ✅ Métricas coletadas
- ✅ Dashboards funcionais
- ✅ Alertas configurados

---

### 7.4. Scripts de Deploy (P2)

**Solução:**
- [ ] Script de deploy para produção
- [ ] Rollback automático
- [ ] Health checks pré-deploy
- [ ] Backup de banco

**deploy.sh:**
```bash
#!/bin/bash
set -e

echo "🚀 Iniciando deploy..."

# Backup do banco
docker exec mysql mysqldump -u root -proot gestaosaudemental_api > backup.sql

# Pull da nova imagem
docker-compose pull app

# Deploy com zero downtime
docker-compose up -d --no-deps app

# Health check
sleep 10
curl -f http://localhost:8080/actuator/health || (docker-compose rollback && exit 1)

echo "✅ Deploy concluído com sucesso!"
```

**Critério de Sucesso:**
- ✅ Deploy automatizado
- ✅ Zero downtime
- ✅ Rollback funciona

---

## 🏗️ FASE 8: ARQUITETURA HEXAGONAL

**Duração:** 2 semanas
**Prioridade:** P2 (Média)
**Objetivo:** Migrar para Clean Architecture / Hexagonal

### 8.1. Reestruturar Pacotes (P2)

**Nova estrutura:**
```
src/main/java/gestaoSaudeMental/api/
├── application/              # Casos de uso
│   ├── service/
│   └── usecase/
├── domain/                   # Núcleo do negócio
│   ├── model/
│   ├── repository/           # Interfaces (Ports)
│   ├── service/
│   └── exception/
├── infrastructure/           # Adapters
│   ├── persistence/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── mapper/
│   ├── security/
│   └── messaging/
└── presentation/             # Controllers
    ├── controller/
    ├── dto/
    └── exception/
```

**Critério de Sucesso:**
- ✅ Separação clara de camadas
- ✅ Domínio independente
- ✅ Testes não quebram

---

### 8.2. Implementar Ports e Adapters (P2)

**Solução:**
- [ ] Criar interfaces no domínio
- [ ] Implementar adapters na infra
- [ ] Inversão de dependência

**Exemplo:**
```java
// Domain - Port (interface)
public interface UsuarioRepository {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
}

// Infrastructure - Adapter (implementação)
@Repository
public class UsuarioJpaAdapter implements UsuarioRepository {
    @Autowired
    private UsuarioJpaRepository jpaRepository;

    @Autowired
    private UsuarioMapper mapper;

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }
}
```

**Critério de Sucesso:**
- ✅ Domínio não depende de infra
- ✅ Fácil trocar implementações

---

### 8.3. Use Cases (P3)

**Solução:**
- [ ] Criar use cases específicos
- [ ] Cada use case = 1 operação de negócio

**Exemplo:**
```java
@Service
public class CadastrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final CriptografiaService criptografiaService;

    public DadosUsuarioCriadoDTO executar(DadosCadastroUsuario dados) {
        // Lógica de negócio pura
    }
}
```

**Critério de Sucesso:**
- ✅ Use cases implementados
- ✅ Controllers chamam use cases

---

## 🌟 FASE 9: RECURSOS AVANÇADOS

**Duração:** 3 semanas
**Prioridade:** P3 (Baixa)
**Objetivo:** Funcionalidades premium

### 9.1. Notificações (P3)

**Solução:**
- [ ] Lembretes diários por email
- [ ] Alertas de padrões negativos
- [ ] Spring Email ou SendGrid

**Dependência:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

**Critério de Sucesso:**
- ✅ Emails enviados
- ✅ Templates bonitos

---

### 9.2. Integração com APIs Externas (P3)

**Solução:**
- [ ] API de frases motivacionais
- [ ] API de notícias de saúde mental
- [ ] OAuth2 (Google, Facebook)

**Critério de Sucesso:**
- ✅ Integração funciona
- ✅ Fallback em caso de erro

---

### 9.3. WebSockets (P3)

**Solução:**
- [ ] Notificações em tempo real
- [ ] Chat com profissionais

**Dependência:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

**Critério de Sucesso:**
- ✅ WebSocket conecta
- ✅ Mensagens em tempo real

---

### 9.4. Frontend (P3)

**Solução:**
- [ ] SPA com React ou Vue
- [ ] Dashboard de estatísticas
- [ ] Gráficos interativos
- [ ] PWA (Progressive Web App)

**Stack sugerida:**
- React 18
- TypeScript
- Tailwind CSS
- Chart.js
- React Query

**Critério de Sucesso:**
- ✅ Frontend funcional
- ✅ Integrado com API

---

## 📅 CRONOGRAMA E ESTIMATIVAS

### Resumo por Fase

| Fase | Duração | Esforço (horas) | Prioridade |
|------|---------|-----------------|------------|
| 1. Fundação | 2 semanas | 60h | P0 |
| 2. Segurança | 1 semana | 30h | P0 |
| 3. Documentação | 1 semana | 25h | P1 |
| 4. Testes | 2 semanas | 50h | P1 |
| 5. Performance | 1 semana | 30h | P1 |
| 6. Analytics | 2 semanas | 40h | P2 |
| 7. Infraestrutura | 2 semanas | 45h | P1 |
| 8. Arquitetura | 2 semanas | 50h | P2 |
| 9. Recursos Avançados | 3 semanas | 60h | P3 |
| **TOTAL** | **16 semanas** | **390h** | - |

### Cronograma Detalhado

```
Mês 1 (Semanas 1-4):
├─ Semana 1: Fase 1 (parte 1) - Fundação
├─ Semana 2: Fase 1 (parte 2) - Fundação
├─ Semana 3: Fase 2 - Segurança
└─ Semana 4: Fase 3 - Documentação

Mês 2 (Semanas 5-8):
├─ Semana 5: Fase 4 (parte 1) - Testes
├─ Semana 6: Fase 4 (parte 2) - Testes
├─ Semana 7: Fase 5 - Performance
└─ Semana 8: Fase 6 (parte 1) - Analytics

Mês 3 (Semanas 9-12):
├─ Semana 9: Fase 6 (parte 2) - Analytics
├─ Semana 10: Fase 7 (parte 1) - Infraestrutura
├─ Semana 11: Fase 7 (parte 2) - Infraestrutura
└─ Semana 12: Fase 8 (parte 1) - Arquitetura

Mês 4 (Semanas 13-16):
├─ Semana 13: Fase 8 (parte 2) - Arquitetura
├─ Semana 14: Fase 9 (parte 1) - Recursos Avançados
├─ Semana 15: Fase 9 (parte 2) - Recursos Avançados
└─ Semana 16: Fase 9 (parte 3) - Recursos Avançados + Polimento
```

### Marcos (Milestones)

| Marco | Data Estimada | Entregas |
|-------|---------------|----------|
| **M1: MVP Refatorado** | Fim Semana 4 | Fundação + Segurança + Documentação |
| **M2: Produção Ready** | Fim Semana 8 | + Testes + Performance + Parte Analytics |
| **M3: Enterprise Grade** | Fim Semana 12 | + Infraestrutura + Arquitetura |
| **M4: Feature Complete** | Fim Semana 16 | + Recursos Avançados |

---

## ✅ CRITÉRIOS DE SUCESSO

### Critérios Técnicos

- [ ] Cobertura de testes > 80%
- [ ] Tempo de resposta < 200ms (P95)
- [ ] Zero vulnerabilidades críticas (OWASP)
- [ ] Build automático funcional
- [ ] Logs estruturados em produção
- [ ] Monitoramento ativo
- [ ] Documentação completa

### Critérios de Qualidade

- [ ] Código segue padrões (SonarQube > 80%)
- [ ] Sem code smells críticos
- [ ] Arquitetura clara e documentada
- [ ] APIs RESTful bem projetadas
- [ ] Tratamento de erros consistente

### Critérios de Portfólio

- [ ] README profissional com badges
- [ ] Diagramas de arquitetura
- [ ] Swagger funcional
- [ ] Deploy automatizado
- [ ] Projeto dockerizado
- [ ] CI/CD configurado

### Critérios de Negócio

- [ ] Todas as funcionalidades originais mantidas
- [ ] Novas funcionalidades agregam valor
- [ ] Performance melhorada
- [ ] Segurança reforçada

---

## 📈 MÉTRICAS DE ACOMPANHAMENTO

### Métricas de Código

| Métrica | Atual | Meta | Como Medir |
|---------|-------|------|------------|
| Cobertura de Testes | < 5% | > 80% | JaCoCo |
| Duplicação | Baixa | < 3% | SonarQube |
| Complexidade | Média | < 10 | SonarQube |
| Vulnerabilidades | ? | 0 | OWASP Dependency Check |
| Tech Debt | Alto | < 5% | SonarQube |

### Métricas de Performance

| Métrica | Atual | Meta | Como Medir |
|---------|-------|------|------------|
| Tempo de Resposta (P95) | ? | < 200ms | Prometheus |
| Throughput | ? | > 100 req/s | JMeter |
| CPU Usage | ? | < 70% | Prometheus |
| Memory Usage | ? | < 80% | Prometheus |
| Cache Hit Rate | 0% | > 80% | Redis Metrics |

### Métricas de Qualidade

| Métrica | Atual | Meta | Como Medir |
|---------|-------|------|------------|
| Build Success Rate | ? | > 95% | GitHub Actions |
| Deploy Frequency | Manual | Daily | GitHub Actions |
| Lead Time | ? | < 1 dia | GitHub Actions |
| MTTR | ? | < 1 hora | Logs + Monitoring |

---

## 🔄 PROCESSO DE ATUALIZAÇÃO

### Como Usar Este Roadmap

1. **Escolher Fase:** Começar pela Fase 1 (obrigatória)
2. **Criar Branch:** `git checkout -b fase-1-fundacao`
3. **Implementar:** Seguir checklist da fase
4. **Testar:** Garantir testes passam
5. **Commitar:** Commits descritivos
6. **PR:** Pull request com descrição detalhada
7. **Review:** Auto-review ou peer review
8. **Merge:** Merge para main
9. **Tag:** Criar tag (ex: `v1.1.0-fase1`)
10. **Próxima Fase:** Repetir processo

### Formato de Commits

```
<tipo>(<escopo>): <descrição>

[corpo opcional]

[rodapé opcional]
```

**Tipos:**
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `refactor`: Refatoração
- `test`: Adicionar testes
- `docs`: Documentação
- `chore`: Tarefas de manutenção
- `perf`: Melhoria de performance

**Exemplos:**
```
feat(service): implementar UsuarioService com lógica de negócio

- Mover lógica de cadastro do controller para service
- Adicionar validação de email único
- Implementar transações

Closes #15
```

---

## 🎓 RECURSOS DE APRENDIZADO

### Livros Recomendados

- Clean Architecture (Robert C. Martin)
- Domain-Driven Design (Eric Evans)
- Spring Boot in Action (Craig Walls)
- Test Driven Development (Kent Beck)

### Cursos Online

- Alura: Spring Boot 3
- Udemy: Spring Security
- Pluralsight: Microservices Architecture
- Coursera: Software Architecture

### Documentação Oficial

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Docs](https://kubernetes.io/docs/)

---

## 📞 SUPORTE E COMUNIDADE

### Onde Buscar Ajuda

- Stack Overflow: Tags `spring-boot`, `java`, `jpa`
- Reddit: r/java, r/springboot
- Discord: Spring Community
- GitHub Issues: Do próprio projeto

### Contribuindo

Se outras pessoas quiserem contribuir:

1. Fork o projeto
2. Criar branch (`git checkout -b feature/AmazingFeature`)
3. Commit mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para branch (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

---

## 📝 CHANGELOG

### v1.0.0 - Estado Inicial (17/11/2025)
- ✅ Cadastro de usuários
- ✅ Autenticação com Spring Security
- ✅ Registro de estados emocionais
- ✅ Histórico emocional
- ✅ Mensagens motivacionais
- ✅ Migrations com Flyway

### v1.1.0 - Fundação (Meta: Semana 2)
- [ ] Camada de Service
- [ ] Global Exception Handler
- [ ] Logging estruturado
- [ ] Configurações externalizadas

### v1.2.0 - Segurança (Meta: Semana 3)
- [ ] JWT implementado
- [ ] CORS configurado
- [ ] Rate limiting

### v2.0.0 - Produção Ready (Meta: Semana 8)
- [ ] Testes > 80%
- [ ] Cache com Redis
- [ ] Documentação Swagger
- [ ] Analytics básicas

### v3.0.0 - Enterprise (Meta: Semana 12)
- [ ] Docker + Docker Compose
- [ ] CI/CD pipeline
- [ ] Monitoramento
- [ ] Arquitetura Hexagonal

### v4.0.0 - Feature Complete (Meta: Semana 16)
- [ ] Notificações
- [ ] Recursos avançados
- [ ] Frontend (opcional)

---

## 🏁 CONCLUSÃO

Este roadmap é um guia vivo e deve ser atualizado conforme:
- Novas necessidades surgirem
- Tecnologias evoluírem
- Feedbacks forem recebidos
- Prioridades mudarem

**Lembre-se:** O objetivo não é apenas implementar features, mas **aprender** e **demonstrar habilidades** para o mercado.

**Bom desenvolvimento! 🚀**

---

**Última Atualização:** 17/11/2025
**Próxima Revisão:** A cada milestone concluído
**Autor:** Desenvolvedor do Projeto
**Versão do Roadmap:** 1.0.0
