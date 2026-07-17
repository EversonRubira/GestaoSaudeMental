# Investigação de Branches Abandonadas — GestaoSaudeMental

**Data da investigação:** 2026-07-17
**Metodologia:** execução real (build, subida da aplicação, chamadas HTTP reais via curl, `mvn test`), não leitura de código. MySQL local (`localhost:3306`, schema `gestaosaudemental_api`) usado como banco de dados. Java 17, Maven 3.9.9, Spring Boot 3.3.4.

**Observação de contexto:** a pasta `api/` local já continha um `.git` íntegro com histórico completo (não estava com histórico perdido, ao contrário do que se supunha inicialmente). O repositório foi mantido como está; apenas `git fetch --all` foi executado para trazer as 4 branches remotas. HEAD de `main` no momento da investigação: commit `4db315e` ("feat: JWT auth, error handling, REST responses, bug fixes").

---

## claude/improve-project-architecture-018CfwQkLBB1nfhh5k9Fj7rV

- **Commits (main..branch):**
  - `1c94bcc` docs: adicionar guia de uso do snapshot
  - `f285780` Merge snapshot documentation into main development branch
  - `1c76633` docs: criar snapshot v1.0.0 do estado original do projeto
- **Build:** sucesso (`mvn clean install` → BUILD SUCCESS, 16s). Os 3 commits desta branch adicionam **apenas documentação** (`README_SNAPSHOT.md`, `ROADMAP.md`, `SNAPSHOT.md`) — nenhuma mudança de código-fonte. O `git diff --stat` contra `main` mostra divergência em arquivos de código (controllers, security, etc.) porque `main` evoluiu depois que esta branch foi criada, não porque a branch alterou esses arquivos.
- **Execução:** subiu com sucesso (`mvn spring-boot:run`, Tomcat na porta 8080, Flyway validou 6 migrations).
- **Teste manual de endpoint:**
  - `POST /usuarios` (cadastro) → **403 Forbidden** ("Access Denied"). O endpoint `/usuarios/**` exige autenticação na `SecurityConfiguration`, mas não há como se autenticar antes de existir uma conta — bloqueio circular.
  - `POST /login` com usuário inexistente → 403 (esperado).
  - `POST /login` com usuário inserido diretamente no banco (contornando o bug acima) → **200 OK, mas corpo vazio**. Login autentica mas não retorna token JWT algum, apesar de existir uma classe `TokenService` no projeto (não usada pelo `AutenticacaoController` desta branch).
- **mvn test:** passa (1 teste — `ApiApplicationTests`, apenas verifica que o contexto Spring sobe).
- **Observação:** branch nomeada "improve-project-architecture" mas não contém nenhuma melhoria de arquitetura de código — é puramente documental. O código-fonte é efetivamente uma versão mais antiga do que já existia, sem o JWT que `main` tem hoje.

---

## claude/project-analysis-improvements-013D8u2Abn921zEwwCq9xUng

- **Commits (main..branch):**
  - `86082d5` docs: Adicionar guias completos de teste e uso da API
  - `2cce9a3` feat: Implementar pacote 'Fundação Sólida' com melhorias críticas
- **Build:** sucesso (`mvn clean install` → BUILD SUCCESS, 18s). Mudanças substanciais: `JwtService`, `JwtAuthenticationFilter`, `GlobalExceptionHandler`, `UsuarioService`/`HistoricoEmocionalService` (camada de serviço extraída dos controllers), SpringDoc/Swagger config, `GUIA_TESTE.md`, coleção Postman, `test-api.sh`.
- **Execução:** subiu com sucesso.
- **Teste manual de endpoint:**
  - `POST /usuarios` (cadastro) → **500 Internal Server Error**. Stack trace real capturado no log:
    ```
    org.springframework.orm.jpa.JpaSystemException: could not execute statement
    [Field 'login' doesn't have a default value]
    [insert into credenciais (senha,id) values (?,?)]
    ```
    Bug real de mapeamento JPA: a coluna `login` (NOT NULL, sem default) da tabela `credenciais` não é preenchida pelo `insert`, porque a entidade `Credenciais` não tem um campo `@Column` para `login` — o valor é derivado via método (`getLogin()`), então o Hibernate nunca o grava. Nenhum registro órfão ficou no banco (rollback funcionou corretamente).
  - `POST /login` com usuário inexistente → 401 estruturado (`{"status":401,"error":"Unauthorized","message":"Email ou senha incorretos."}`) — tratamento de erro mais limpo que na branch anterior.
  - `POST /login` com usuário inserido manualmente no banco → **200 OK, com JWT real**: `{"token":"eyJhbGci...","type":"Bearer","expiresIn":86400000}`.
  - `GET /usuarios/{id}/historico-cronologico` com o token → **200 OK, `[]`** (endpoint protegido funcionando corretamente com o token).
  - Mesmo endpoint sem token → **403 Forbidden**.
- **mvn test:** passa (1 teste, apenas contexto).
- **Observação:** JWT funcional de ponta a ponta via banco populado manualmente, mas o cadastro via API está completamente quebrado por um bug de mapeamento JPA — ninguém consegue se cadastrar organicamente por essa branch.

---

## claude/add-ai-responses-01WLPzvAHHTsU9jD1e1Xqwdc

- **Commits (main..branch):**
  - `279d76d` Implementar respostas com IA para mensagens motivacionais
  - `cc7e23b` Remove conteúdo pessoal sobre apresentação em entrevistas
- **Build:** sucesso (`mvn clean install` → BUILD SUCCESS, 22s). Adiciona `AIService`/`OpenAIService`, `.env.example` (com `AI_ENABLED=false` por padrão — não requer chave da OpenAI para rodar/buildar).
- **Execução:** subiu com sucesso.
- **Teste manual de endpoint:**
  - `POST /usuarios` (cadastro) → **403 Forbidden** ("Access Denied"), mesmo bug circular de autenticação da branch 1 (`SecurityConfiguration` desta branch é idêntica à branch 1 — `TokenService`/`SecurityFilter` também foram removidos aqui em relação a `main`).
  - `POST /login` com usuário inserido manualmente no banco → **200 OK, corpo vazio** (sem JWT, mesmo padrão da branch 1).
  - `POST /usuarios/{id}/historico` (endpoint alvo da funcionalidade de IA) → **403 Forbidden**, pois sem JWT retornado pelo login não há como autenticar a chamada. **A funcionalidade de IA que dá nome à branch está inacessível via HTTP**, confirmado por execução real (não apenas leitura de código).
  - Adicionalmente, por leitura do código-fonte (não confirmável em runtime dado o bloqueio acima): em `UsuarioController.registrarEstadoEmocional`, a variável `mensagem` (resultado de `MensagemMotivacional.obterMensagem(...)`) é calculada mas **nunca incluída na resposta** — o método retorna apenas uma string genérica fixa. Ou seja, mesmo que a autenticação funcionasse, a mensagem motivacional/IA não chegaria ao cliente.
- **mvn test:** passa (1 teste, apenas contexto).
- **Observação:** a feature de "respostas com IA" está presente no código de domínio (`AIService`, `OpenAIService`, `MensagemMotivacional`) mas é **inatingível** pela API pública nesta branch, por dois motivos independentes: (1) bug de autenticação circular idêntico ao da branch 1, e (2) o valor calculado nunca é retornado no `response body`.

---

## claude/android-migration-consideration-017BrNi19nzv5viszzP5xcd7

- **Commits (main..branch):** 7 commits —
  - `e79636c` Adiciona estrutura inicial do projeto Android e documentação de arquitetura
  - `3aedde8` Adiciona matriz completa de sugestões inteligentes (Emoção x Energia x Contexto)
  - `0c4d96e` Implementa sistema de sugestões inteligentes (Emoção x Energia x Contexto)
  - `c77afca` Adiciona suporte a ciclo menstrual e sintomas físicos
  - `014425e` Adiciona notas importantes sobre correção de mal-entendido
  - `5d5a534` Adiciona proteção de arquivos sensíveis
  - `96730ba` Adiciona README profissional para portfólio
- **Build:** **FALHA**. `mvn clean install` e `mvn test` falham ambos com o mesmo erro de compilação:
  ```
  [ERROR] UsuarioController.java:[77,26] no suitable constructor found for
  HistoricoEmocional(<nulltype>, LocalDate, EstadoEmocionalEnum, AtividadeRealizadaEnum, Usuario)
  ```
  A entidade `HistoricoEmocional` foi refatorada nesta mesma branch para um construtor de 11 parâmetros (adicionando `NivelEnergiaEnum`, `List<ContextoEnum>`, `Sugestao`, `FeedbackSugestaoEnum`, `CicloMenstrualEnum`, `List<SintomaFisicoEnum>`), mas o `UsuarioController` não foi atualizado e ainda chama o construtor antigo de 5 parâmetros. O projeto simplesmente **não compila**.
- **Execução:** **não verificado** — impossível subir a aplicação sem build bem-sucedido.
- **Teste manual de endpoint:** **não verificado** — impossível testar sem a aplicação rodando.
- **mvn test:** **falha** (mesmo erro de compilação; nenhum teste chega a rodar).
- **Observação:** branch mais ambiciosa em escopo (sistema de sugestões inteligentes por matriz Emoção×Energia×Contexto, suporte a ciclo menstrual, novas migrations V7–V10, scaffold de um app Android completo em `android-app/`), mas está em estado quebrado — não compila. Não foi possível avaliar nenhuma das features novas em runtime.

---

## Verificação do checklist de `NOTAS_TECNICAS_REFATORACAO.md` (seção "1. PROBLEMAS CRÍTICOS IDENTIFICADOS") contra `main` atual

Documento datado de 2025-10-29. Verificação feita em `main` (commit `4db315e`), com build + execução real e chamadas HTTP reais (não apenas leitura de código).

### 1.1 Segurança Incompleta 🔴 URGENTE — **RESOLVIDO**
- Confirmado por execução: `POST /login` com usuário existente retorna JWT real (`AutenticacaoController` agora usa `TokenService.gerarToken(...)`).
- `GET /usuarios/{id}/historico-cronologico` com o token → 200 OK; sem token → 403 Forbidden. Autenticação/autorização funcionando de ponta a ponta.
- `UserDetailsService` está configurado (log de boot: `Global AuthenticationManager configured with UserDetailsService bean with name autenticacaoService`).
- **Ressalva (bug novo, não listado nas notas técnicas):** o fluxo de **cadastro** (`POST /usuarios`) está quebrado — falha com erro de SQL porque a entidade `Credenciais` não mapeia a coluna `login` (`NOT NULL` sem default) para nenhum campo JPA. Confirmado via stack trace real: `Field 'login' doesn't have a default value`. Isso significa que, embora a autenticação em si funcione, **não é possível criar uma conta nova pela API** — só foi possível testar login inserindo o usuário diretamente no banco via SQL.
- **Bug adicional observado:** `TratadorDeErros.tratarRuntimeException` captura qualquer `RuntimeException` (incluindo a `JpaSystemException` do bug acima) e responde com **404 Not Found**, mascarando um erro de servidor (500) como "não encontrado". Confirmado via `curl`: cadastro retornou `HTTP_STATUS:404` com corpo `{"mensagem":"could not execute statement..."}`.

### 1.2 Bug no Endpoint DELETE 🔴 — **RESOLVIDO**
- `UsuarioController.java` atual: `@DeleteMapping("/{id}")` já está correto.
- Confirmado por execução: `DELETE /usuarios/{id}` com token válido → **204 No Content**.

### 1.3 Campos Redundantes na Entidade Usuario — **AINDA PRESENTE**
- `Usuario.java` ainda tem os campos `estadoEmocional` (`EstadoEmocionalEnum`) e `atividadeRealizada` (`AtividadeRealizadaEnum`), duplicando dados já presentes em `HistoricoEmocional`. Nenhuma migration para remover essas colunas foi encontrada.

### 1.4 Tratamento de Erros Inexistente — **PARCIALMENTE RESOLVIDO**
- Existe `@RestControllerAdvice` (`TratadorDeErros`) com handlers para `EntityNotFoundException`, `RuntimeException`, `IllegalArgumentException`, `MethodArgumentNotValidException`, `BadCredentialsException` — cliente já recebe JSON estruturado em vez de stack trace bruto na maioria dos casos (confirmado: erro de validação retorna lista de campos inválidos; credenciais erradas retornam 401 estruturado).
- Porém, o handler genérico de `RuntimeException` devolve **404 para qualquer erro não mapeado**, incluindo erros de infraestrutura/banco (500 mascarado como 404) — como visto no bug de cadastro acima. Ou seja, a estrutura existe mas a semântica dos códigos HTTP ainda está incorreta para esse caso.

### 1.5 Violações de Princípios REST — **EM GRANDE PARTE RESOLVIDO**
- Controllers agora retornam DTOs estruturados (`DadosUsuarioCriadoDTO`, `DadosRegistroHistoricoResponseDTO`, `ResponseEntity<DadosTokenJWT>`) em vez de `String` cru.
- Códigos HTTP corretos observados na prática: 201 Created (cadastro bem-sucedido, registro de histórico), 204 No Content (delete), 400 Bad Request (validação), 401 Unauthorized (credenciais inválidas), 403 Forbidden (sem token).
- POST não retorna `Location` header nem segue HATEOAS — esses dois pontos específicos não foram implementados, mas não foram testados como bloqueantes.

### 1.6 Falta de Validação — **EM GRANDE PARTE RESOLVIDO**
- `DadosCadastroUsuario` agora tem `@NotBlank`, `@Email`, `@Size`, `@Past`, `@NotNull` em todos os campos relevantes, confirmado por execução: envio de dados inválidos (nome vazio, email malformado, telefone curto, senha curta, data de nascimento futura) retornou **400 Bad Request** com lista detalhada de erros por campo.
- Ressalva: `Usuario.java` (a entidade JPA, não o DTO) ainda tem `@Email` comentado (`//@Email`) acima do campo `email` — validação existe na camada de entrada (DTO), mas não na entidade persistida.

---

## Resumo rápido

| Branch | Build | Execução | Endpoint alvo funcional? |
|---|---|---|---|
| improve-project-architecture | ✅ sucesso | ✅ sobe | ❌ só docs, sem mudança de código; herda bug de cadastro bloqueado |
| project-analysis-improvements | ✅ sucesso | ✅ sobe | ⚠️ JWT funciona (via inserção manual no banco); cadastro via API quebrado (500) |
| add-ai-responses | ✅ sucesso | ✅ sobe | ❌ feature de IA inacessível (403 em cascata + valor nunca retornado no código) |
| android-migration-consideration | ❌ **não compila** | não verificado | não verificado |

Nenhuma conclusão sobre motivo de abandono das branches foi tirada neste documento — apenas fatos observados via execução real.
