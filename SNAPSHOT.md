# 📸 SNAPSHOT v1.0.0 - GESTÃO SAÚDE MENTAL

**Data:** 17 de Novembro de 2025
**Branch:** `snapshot/v1.0.0-original`
**Objetivo:** Snapshot do projeto antes de melhorias arquiteturais e de código

---

## 📋 ÍNDICE

1. [Visão Geral](#visão-geral)
2. [Stack Tecnológico](#stack-tecnológico)
3. [Estrutura do Projeto](#estrutura-do-projeto)
4. [Funcionalidades Implementadas](#funcionalidades-implementadas)
5. [Endpoints da API](#endpoints-da-api)
6. [Banco de Dados](#banco-de-dados)
7. [Segurança](#segurança)
8. [Como Executar](#como-executar)
9. [Estado Atual do Código](#estado-atual-do-código)
10. [Próximas Melhorias](#próximas-melhorias)

---

## 🎯 VISÃO GERAL

### Descrição
Sistema REST API para gerenciamento de saúde mental, permitindo que usuários registrem diariamente seus estados emocionais, visualizem histórico e recebam mensagens motivacionais personalizadas.

### Propósito
- Acompanhar bem-estar emocional ao longo do tempo
- Identificar padrões emocionais
- Fornecer suporte motivacional baseado em estados emocionais
- Criar consciência sobre saúde mental

### Características Principais
- ✅ Autenticação de usuários com Spring Security
- ✅ Registro diário de estados emocionais
- ✅ Histórico emocional com filtros por data e emoção
- ✅ Mensagens motivacionais contextualizadas
- ✅ Exclusão lógica de contas (soft delete)
- ✅ Validação de dados com Jakarta Validation
- ✅ Migrations de banco de dados com Flyway

---

## 💻 STACK TECNOLÓGICO

### Backend
| Tecnologia | Versão | Finalidade |
|-----------|--------|------------|
| **Java** | 17 | Linguagem de programação |
| **Spring Boot** | 3.3.4 | Framework web |
| **Spring Data JPA** | 3.3.4 | Persistência de dados |
| **Spring Security** | 6.3.x | Autenticação e autorização |
| **Spring Validation** | 3.3.4 | Validação de entrada |

### Banco de Dados
| Tecnologia | Versão | Finalidade |
|-----------|--------|------------|
| **MySQL** | 8.0+ | Banco de dados relacional |
| **Flyway** | Latest | Migrations de banco de dados |

### Ferramentas
| Tecnologia | Versão | Finalidade |
|-----------|--------|------------|
| **Maven** | 3.x | Build e gerenciamento de dependências |
| **Lombok** | 1.18.x | Redução de boilerplate |
| **Spring DevTools** | 3.3.4 | Hot reload em desenvolvimento |
| **JUnit 5** | 5.x | Testes unitários |

### Versões das Dependências Principais

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.4</version>
</parent>

<properties>
    <java.version>17</java.version>
</properties>
```

---

## 📁 ESTRUTURA DO PROJETO

```
GestaoSaudeMental/
├── .git/                                    # Controle de versão Git
├── .mvn/                                    # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/gestaoSaudeMental/api/
│   │   │   ├── ApiApplication.java          # Classe principal @SpringBootApplication
│   │   │   │
│   │   │   ├── controller/                  # CAMADA DE APRESENTAÇÃO
│   │   │   │   ├── AutenticacaoController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   └── HelloController.java
│   │   │   │
│   │   │   ├── domain/                      # CAMADA DE DOMÍNIO
│   │   │   │   ├── auth/
│   │   │   │   │   ├── Credenciais.java              # Entity (implementa UserDetails)
│   │   │   │   │   ├── CredenciaisRepository.java    # JPA Repository
│   │   │   │   │   └── AutenticacaoService.java      # UserDetailsService
│   │   │   │   │
│   │   │   │   └── usuario/
│   │   │   │       ├── Usuario.java                  # Entity principal
│   │   │   │       ├── HistoricoEmocional.java       # Entity de histórico
│   │   │   │       ├── UsuarioRepository.java        # JPA Repository
│   │   │   │       ├── HistoricoEmocionalRepository.java
│   │   │   │       │
│   │   │   │       ├── AtividadeRealizadaEnum.java   # Enum
│   │   │   │       ├── EstadoEmocionalEnum.java      # Enum
│   │   │   │       ├── GeneroEnum.java               # Enum
│   │   │   │       │
│   │   │   │       ├── DadosAutenticacao.java        # Record DTO
│   │   │   │       ├── DadosCadastroUsuario.java     # DTO
│   │   │   │       ├── DadosListagemEstadoEmocionalDTO.java
│   │   │   │       ├── DadosRegistroEstadoEmocional.java
│   │   │   │       ├── DadosUsuarioCriadoDTO.java
│   │   │   │       │
│   │   │   │       └── MensagemMotivacional.java     # Classe utilitária
│   │   │   │
│   │   │   └── infra/                       # CAMADA DE INFRAESTRUTURA
│   │   │       └── security/
│   │   │           └── SecurityConfiguration.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties       # Configurações da aplicação
│   │       │
│   │       └── db/migration/                # Flyway Migrations
│   │           ├── V1__create-table-usuarios.sql
│   │           ├── V2__create-table-historico_emocional.sql
│   │           ├── V3__create-table-usuarios_ativos.sql
│   │           ├── V4__update-usuarios-remove-mandatory-fields.sql
│   │           ├── V5__create-table-credenciais.sql
│   │           └── V6__alter_credenciais_to_shared_primary_key.sql
│   │
│   └── test/
│       └── java/gestaoSaudeMental/api/
│           └── ApiApplicationTests.java     # Teste de contexto Spring
│
├── pom.xml                                  # Configuração Maven
├── mvnw / mvnw.cmd                          # Maven Wrapper scripts
├── .gitignore                               # Arquivos ignorados pelo Git
├── README.md                                # Documentação principal
├── SNAPSHOT.md                              # Este arquivo
└── ROADMAP.md                               # Melhorias planejadas

```

### Estatísticas do Projeto
- **Total de arquivos:** 63
- **Arquivos Java:** 22
- **Arquivos SQL (migrations):** 6
- **Tamanho do projeto:** ~307 KB
- **Linhas de código:** ~1.500 (estimado)

---

## ⚙️ FUNCIONALIDADES IMPLEMENTADAS

### 1. Autenticação e Autorização
- ✅ Cadastro de novos usuários
- ✅ Login com email e senha
- ✅ Criptografia de senhas com BCrypt
- ✅ Spring Security configurado
- ✅ Proteção de endpoints sensíveis
- ❌ JWT não implementado (usa sessão)

### 2. Gestão de Usuários
- ✅ Cadastro com validação de dados
- ✅ Email único no sistema
- ✅ Armazenamento de informações pessoais (nome, email, telefone, data nascimento, gênero)
- ✅ Exclusão lógica de contas (campo `ativo`)
- ❌ Não há endpoint de atualização de dados

### 3. Registro de Estados Emocionais
- ✅ Registro diário de estado emocional
- ✅ Associação com atividade realizada
- ✅ Data automática de registro
- ✅ Histórico completo mantido

**Estados Emocionais Disponíveis:**
- FELIZ
- TRISTE
- ANSIOSO
- CANSADO
- IRRITADO
- OUTROS

**Atividades Disponíveis:**
- EXERCICIO
- LEITURA
- TRABALHO
- SOCIALIZAR
- ASSISTIR_TV
- MEDITAR
- OUTROS

### 4. Consulta de Histórico
- ✅ Listagem por período (data início e fim)
- ✅ Listagem cronológica completa
- ✅ Filtro por tipo de emoção
- ✅ Mensagens motivacionais personalizadas em cada registro
- ❌ Sem paginação
- ❌ Sem estatísticas ou análises

### 5. Mensagens Motivacionais
- ✅ Mensagens contextualizadas baseadas em emoção + atividade
- ✅ Nome do usuário personalizado nas mensagens
- ✅ Fallback para combinações não mapeadas
- ⚠️ Cobertura parcial de combinações emoção/atividade

### 6. Validações
- ✅ Nome obrigatório
- ✅ Email válido e obrigatório
- ✅ Telefone entre 10-15 dígitos
- ✅ Data de nascimento no passado
- ✅ Gênero obrigatório
- ✅ Validação automática via `@Valid`

---

## 🌐 ENDPOINTS DA API

### Base URL
```
http://localhost:8080
```

### 1. Autenticação

#### POST /login
Efetuar login no sistema

**Request:**
```http
POST /login HTTP/1.1
Content-Type: application/json

{
  "login": "usuario@example.com",
  "senha": "minhasenha123"
}
```

**Response Success (200 OK):**
```json
{}
```

**Response Error (401 Unauthorized):**
```json
{
  "error": "Unauthorized"
}
```

---

### 2. Usuários

#### POST /usuarios
Cadastrar novo usuário

**Autenticação:** Não requerida

**Request:**
```http
POST /usuarios HTTP/1.1
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@example.com",
  "telefone": "11987654321",
  "dataNascimento": "1990-05-15",
  "genero": "MASCULINO",
  "senha": "minhasenha123"
}
```

**Validações:**
- `nome`: Não pode ser vazio
- `email`: Deve ser válido e único no sistema
- `telefone`: Entre 10 e 15 dígitos
- `dataNascimento`: Deve ser no passado
- `genero`: MASCULINO, FEMININO, NAO_BINARIO, OUTROS
- `senha`: Será criptografada com BCrypt

**Response Success (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva"
}
```

**Response Error (400 Bad Request):**
```json
{
  "timestamp": "2025-11-17T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "O email deve ser válido."
    }
  ]
}
```

---

#### DELETE /usuarios?id={id}
Desativar conta de usuário (exclusão lógica)

**Autenticação:** Requerida

**Request:**
```http
DELETE /usuarios?id=1 HTTP/1.1
```

**Response Success (204 No Content):**
```
(sem corpo)
```

**Response Error (404 Not Found):**
```json
{
  "error": "Usuario nao Cadastrado"
}
```

---

### 3. Histórico Emocional

#### POST /usuarios/{id}/historico
Registrar novo estado emocional

**Autenticação:** Requerida

**Request:**
```http
POST /usuarios/1/historico HTTP/1.1
Content-Type: application/json

{
  "estadoEmocional": "FELIZ",
  "atividadeRealizada": "EXERCICIO"
}
```

**Response Success (201 Created):**
```
"Registro de estado emocional criado com sucesso para o usuário com ID 1."
```

**Response Error (404 Not Found):**
```
"Usuario nao Cadastrado"
```

---

#### GET /usuarios/{id}/historico_por_periodo
Listar histórico por período

**Autenticação:** Requerida

**Request:**
```http
GET /usuarios/1/historico_por_periodo?inicio=2024-01-01&fim=2024-12-31 HTTP/1.1
```

**Query Parameters:**
- `inicio` (required): Data de início (formato: YYYY-MM-DD)
- `fim` (required): Data de fim (formato: YYYY-MM-DD)

**Validações:**
- Data início não pode ser maior que data fim

**Response Success (200 OK):**
```json
[
  {
    "nome": "João Silva",
    "dataRegistro": "2024-01-15",
    "estadoEmocional": "FELIZ",
    "atividadeRealizada": "EXERCICIO",
    "mensagemMotivacional": "João, mantenha essa energia positiva enquanto se exercita!"
  },
  {
    "nome": "João Silva",
    "dataRegistro": "2024-02-20",
    "estadoEmocional": "ANSIOSO",
    "atividadeRealizada": "MEDITAR",
    "mensagemMotivacional": "João, a meditação é uma ótima maneira de acalmar a mente."
  }
]
```

**Response Error (400 Bad Request):**
```
"A data de início não pode ser maior que a data de fim."
```

---

#### GET /usuarios/{id}/historico-cronologico
Listar histórico em ordem cronológica

**Autenticação:** Requerida

**Request:**
```http
GET /usuarios/1/historico-cronologico HTTP/1.1
```

**Query Parameters (opcionais):**
- `emocao`: Filtrar por estado emocional específico (ex: TRISTE, FELIZ, ANSIOSO)

**Exemplos:**
```http
# Todos os registros
GET /usuarios/1/historico-cronologico HTTP/1.1

# Apenas registros de tristeza
GET /usuarios/1/historico-cronologico?emocao=TRISTE HTTP/1.1
```

**Response Success (200 OK):**
```json
[
  {
    "nome": "João Silva",
    "dataRegistro": "2024-01-05",
    "estadoEmocional": "TRISTE",
    "atividadeRealizada": "ASSISTIR_TV",
    "mensagemMotivacional": "João, permita-se relaxar enquanto assiste algo que você gosta."
  },
  {
    "nome": "João Silva",
    "dataRegistro": "2024-01-15",
    "estadoEmocional": "FELIZ",
    "atividadeRealizada": "EXERCICIO",
    "mensagemMotivacional": "João, mantenha essa energia positiva enquanto se exercita!"
  }
]
```

---

### 4. Health Check

#### GET /hello
Endpoint de teste/health check

**Autenticação:** Não requerida

**Request:**
```http
GET /hello HTTP/1.1
```

**Response Success (200 OK):**
```
Hello World Srping Test
```

---

## 🗄️ BANCO DE DADOS

### Configuração (application.properties)

```properties
spring.application.name=api

# MySQL Database
spring.datasource.url=jdbc:mysql://localhost:3306/gestaosaudemental_api
spring.datasource.username=root
spring.datasource.password=root

# Flyway Migrations
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

### Modelo de Dados (ER Diagram)

```
┌─────────────────────────────────────┐
│           usuarios                  │
├─────────────────────────────────────┤
│ id (PK)                    BIGINT   │
│ nome                       VARCHAR  │
│ email (UNIQUE)             VARCHAR  │
│ telefone                   VARCHAR  │
│ data_nascimento            DATE     │
│ genero                     ENUM     │
│ estado_emocional           VARCHAR  │ (nullable)
│ atividade_realizada        VARCHAR  │ (nullable)
│ ativo                      BOOLEAN  │
└────────────┬────────────────────────┘
             │
             │ (1:1)
             │
┌────────────▼────────────────────────┐
│         credenciais                 │
├─────────────────────────────────────┤
│ id (PK, FK) → usuarios.id  BIGINT   │
│ login                      VARCHAR  │
│ senha (BCrypt)             VARCHAR  │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│           usuarios                  │
└────────────┬────────────────────────┘
             │
             │ (1:N)
             │
┌────────────▼────────────────────────┐
│     historico_emocional             │
├─────────────────────────────────────┤
│ id (PK)                    BIGINT   │
│ data_registro              DATE     │
│ estado_emocional           ENUM     │
│ atividade_realizada        ENUM     │
│ usuario_id (FK)            BIGINT   │
└─────────────────────────────────────┘
```

### Tabelas

#### 1. usuarios
Armazena informações dos usuários cadastrados.

```sql
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    data_nascimento DATE NOT NULL,
    genero ENUM('MASCULINO', 'FEMININO', 'NAO_BINARIO', 'OUTROS') DEFAULT 'OUTROS',
    estado_emocional VARCHAR(255) NULL,
    atividade_realizada VARCHAR(255) NULL,
    ativo BOOLEAN DEFAULT TRUE
);
```

**Campos:**
- `id`: Identificador único (auto incremento)
- `email`: Único no sistema (constraint UNIQUE)
- `ativo`: Exclusão lógica (FALSE = conta desativada)

#### 2. credenciais
Armazena credenciais de autenticação (relacionamento 1:1 com usuarios).

```sql
CREATE TABLE credenciais (
    id BIGINT NOT NULL PRIMARY KEY,
    login VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES usuarios(id)
);
```

**Características:**
- ID compartilhado com usuarios (shared primary key)
- Senha armazenada com hash BCrypt
- Login é o email do usuário

#### 3. historico_emocional
Registra o histórico de estados emocionais dos usuários.

```sql
CREATE TABLE historico_emocional (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_registro DATE NOT NULL,
    estado_emocional ENUM('FELIZ', 'TRISTE', 'ANSIOSO', 'CANSADO', 'IRRITADO', 'OUTROS') NOT NULL,
    atividade_realizada ENUM('EXERCICIO', 'LEITURA', 'TRABALHO', 'SOCIALIZAR', 'ASSISTIR_TV', 'MEDITAR', 'OUTROS') NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
```

**Características:**
- Relacionamento N:1 com usuarios
- Data de registro automática
- Estados e atividades como ENUM

### Migrations (Flyway)

Histórico de evolução do banco de dados:

| Versão | Arquivo | Descrição |
|--------|---------|-----------|
| V1 | `V1__create-table-usuarios.sql` | Criação inicial da tabela usuarios |
| V2 | `V2__create-table-historico_emocional.sql` | Criação da tabela de histórico |
| V3 | `V3__create-table-usuarios_ativos.sql` | Adição do campo `ativo` para soft delete |
| V4 | `V4__update-usuarios-remove-mandatory-fields.sql` | Campos estado_emocional e atividade_realizada tornaram-se nullable |
| V5 | `V5__create-table-credenciais.sql` | Criação da tabela de credenciais |
| V6 | `V6__alter_credenciais_to_shared_primary_key.sql` | Alteração para shared primary key (1:1 com usuarios) |

### Índices Recomendados (não implementados)

```sql
-- Melhorar performance em buscas
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_ativo ON usuarios(ativo);
CREATE INDEX idx_historico_usuario_id ON historico_emocional(usuario_id);
CREATE INDEX idx_historico_data ON historico_emocional(data_registro);
CREATE INDEX idx_historico_usuario_data ON historico_emocional(usuario_id, data_registro);
```

---

## 🔐 SEGURANÇA

### Autenticação

**Método Atual:** Spring Security com UserDetailsService

**Fluxo:**
1. Usuário envia email e senha para `/login`
2. `AuthenticationManager` processa autenticação
3. `AutenticacaoService` busca credenciais no banco via email
4. `BCryptPasswordEncoder` valida senha
5. Spring Security cria sessão autenticada
6. Usuário pode acessar endpoints protegidos

**Limitações:**
- ❌ Não usa JWT (dificulta APIs stateless)
- ❌ Sessão baseada em servidor (não escalável horizontalmente)
- ❌ Sem refresh tokens
- ❌ Sem expiração explícita de tokens

### Autorização

**Configuração (SecurityConfiguration.java):**

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/usuarios/**").authenticated()  // ✅ Protegido
            .anyRequest().permitAll()                         // ✅ Público
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
}
```

**Endpoints Protegidos:**
- `POST /usuarios/{id}/historico`
- `GET /usuarios/{id}/historico_por_periodo`
- `GET /usuarios/{id}/historico-cronologico`
- `DELETE /usuarios`

**Endpoints Públicos:**
- `POST /login`
- `POST /usuarios` (cadastro)
- `GET /hello`

### Criptografia de Senhas

**Algoritmo:** BCrypt (Blowfish cipher)

**Características:**
- ✅ Salt automático (proteção contra rainbow tables)
- ✅ Adaptável (pode aumentar complexidade no futuro)
- ✅ Industry standard

**Implementação:**
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// No cadastro
String senhaHash = passwordEncoder.encode(dados.senha());

// No login (automático pelo Spring Security)
passwordEncoder.matches(rawPassword, encodedPassword);
```

### Vulnerabilidades Conhecidas

⚠️ **Problemas de Segurança a Resolver:**

1. **CSRF Desabilitado**
   - Configuração atual: `http.csrf().disable()`
   - Risco: Ataques Cross-Site Request Forgery
   - Justificativa temporária: API REST stateless

2. **Sem CORS Configurado**
   - Qualquer origem pode acessar a API
   - Necessário configurar origins permitidas

3. **Sem Rate Limiting**
   - Vulnerável a ataques de força bruta no login
   - Sem proteção contra DoS

4. **Credenciais no application.properties**
   - Senha do banco hardcoded
   - Deve usar variáveis de ambiente

5. **Sem HTTPS Enforced**
   - Dados transitam sem criptografia
   - Necessário configurar TLS/SSL em produção

6. **Sem Auditoria**
   - Não registra quem criou/modificou registros
   - Dificulta rastreabilidade

7. **Sem Validação de Força de Senha**
   - Aceita senhas fracas
   - Recomendado: mínimo 8 caracteres, letras+números+símbolos

---

## 🚀 COMO EXECUTAR

### Pré-requisitos

- **Java JDK 17+** instalado
- **MySQL 8.0+** instalado e rodando
- **Maven 3.x** (ou usar mvnw incluído)
- **Git** para clonar o repositório

### 1. Clonar o Repositório

```bash
git clone https://github.com/EversonRubira/GestaoSaudeMental.git
cd GestaoSaudeMental
```

### 2. Configurar Banco de Dados

**Criar database no MySQL:**

```sql
CREATE DATABASE gestaosaudemental_api;
```

**Verificar configurações em `application.properties`:**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestaosaudemental_api
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Executar a Aplicação

**Opção A: Com Maven instalado**

```bash
mvn spring-boot:run
```

**Opção B: Com Maven Wrapper (recomendado)**

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

**Opção C: Build e execução de JAR**

```bash
# Compilar
mvn clean package

# Executar
java -jar target/api-0.0.1-SNAPSHOT.jar
```

### 4. Verificar Execução

A aplicação estará rodando em: `http://localhost:8080`

**Testar com endpoint público:**

```bash
curl http://localhost:8080/hello
```

Deve retornar: `Hello World Srping Test`

### 5. Testar Endpoints

**Cadastrar usuário:**

```bash
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "telefone": "11987654321",
    "dataNascimento": "1990-05-15",
    "genero": "MASCULINO",
    "senha": "senha123"
  }'
```

**Fazer login:**

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao@example.com",
    "senha": "senha123"
  }'
```

**Registrar estado emocional:**

```bash
curl -X POST http://localhost:8080/usuarios/1/historico \
  -H "Content-Type: application/json" \
  -d '{
    "estadoEmocional": "FELIZ",
    "atividadeRealizada": "EXERCICIO"
  }'
```

### Solução de Problemas

**Erro: Port 8080 already in use**
```properties
# Alterar porta em application.properties
server.port=8081
```

**Erro: Access denied for user 'root'**
- Verificar credenciais do MySQL
- Criar usuário específico para a aplicação

**Flyway migration failed**
- Limpar banco e recriar
- Verificar se todas as migrations estão corretas

---

## 📊 ESTADO ATUAL DO CÓDIGO

### Arquitetura

**Padrão:** Layered Architecture (Arquitetura em Camadas)

```
┌─────────────────────┐
│    Controllers      │  → Recebe requisições HTTP
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│  Repositories       │  → Acesso direto a dados (JPA)
└──────────┬──────────┘
           │
┌──────────▼──────────┐
│    Entities         │  → Modelos de domínio
└─────────────────────┘
```

**Características:**
- ✅ Separação clara entre controllers e repositories
- ⚠️ **PROBLEMA:** Lógica de negócio nos controllers
- ⚠️ **PROBLEMA:** Ausência de camada de Service
- ⚠️ **PROBLEMA:** Controllers acessam múltiplos repositories diretamente

### Qualidade do Código

#### ✅ Pontos Fortes

1. **Uso de DTOs**
   - Separação entre entidades e objetos de transferência
   - Records para DTOs imutáveis

2. **Validação Declarativa**
   - Jakarta Validation bem aplicada
   - Mensagens de erro customizadas

3. **Enumerações**
   - Tipo-segurança para estados e atividades
   - Previne valores inválidos

4. **Lombok**
   - Reduz boilerplate
   - Código mais limpo

5. **Migrations de Banco**
   - Flyway gerencia versões
   - Histórico de evolução do schema

6. **Relacionamentos JPA**
   - OneToOne e ManyToOne bem definidos
   - Uso correto de @MapsId

#### ⚠️ Problemas Identificados

1. **Lógica de Negócio nos Controllers**
   ```java
   // ❌ Controller fazendo múltiplas responsabilidades
   @PostMapping
   @Transactional
   public ResponseEntity<DadosUsuarioCriadoDTO> cadastrar(...) {
       Usuario usuario = new Usuario(dados);
       repository.save(usuario);

       String senhaHash = passwordEncoder.encode(dados.senha());
       Credenciais credenciais = new Credenciais(usuario, senhaHash);
       credenciaisRepository.save(credenciais);

       return ResponseEntity.created(...).body(...);
   }
   ```

2. **Tratamento de Exceções Genérico**
   ```java
   // ❌ RuntimeException genérica
   throw new RuntimeException("Usuario nao Cadastrado");
   ```

3. **Ausência de Logging**
   - Nenhum log implementado
   - Dificulta debugging

4. **Sem Testes Unitários**
   - Apenas teste de contexto Spring
   - Cobertura < 5%

5. **Configurações Hardcoded**
   ```properties
   # ❌ Credenciais no código
   spring.datasource.password=root
   ```

6. **Sem Documentação de API**
   - Sem Swagger/OpenAPI
   - Apenas README básico

7. **Mensagens Motivacionais Incompletas**
   - Apenas algumas combinações mapeadas
   - Código poderia ser mais robusto

### Métricas de Código

| Métrica | Valor Atual | Alvo Ideal |
|---------|-------------|------------|
| Cobertura de Testes | < 5% | > 80% |
| Complexidade Ciclomática | Média | Baixa |
| Duplicação de Código | Baixa | Baixa ✅ |
| Documentação | Básica | Completa |
| Camadas Arquiteturais | 2 | 4+ |

### Dívida Técnica

**Alta Prioridade:**
- [ ] Implementar camada de Service
- [ ] Criar Global Exception Handler
- [ ] Adicionar logging estruturado
- [ ] Implementar JWT

**Média Prioridade:**
- [ ] Adicionar testes unitários
- [ ] Implementar Swagger/OpenAPI
- [ ] Externalizar configurações
- [ ] Adicionar cache

**Baixa Prioridade:**
- [ ] Refatorar para arquitetura hexagonal
- [ ] Implementar métricas (Actuator)
- [ ] Adicionar CI/CD
- [ ] Containerização (Docker)

---

## 🎯 PRÓXIMAS MELHORIAS

Ver [ROADMAP.md](ROADMAP.md) para plano completo de melhorias.

### Melhorias Críticas

1. **Implementar Camada de Service**
   - Mover lógica de negócio dos controllers
   - Single Responsibility Principle

2. **Global Exception Handler**
   - Custom exceptions
   - Respostas de erro padronizadas

3. **JWT Authentication**
   - API stateless
   - Tokens com expiração

4. **Testes Automatizados**
   - Unitários (services)
   - Integração (controllers)
   - Cobertura > 80%

5. **Documentação com Swagger**
   - OpenAPI 3.0
   - Exemplos de requisição/resposta

### Melhorias de Performance

1. **Cache com Redis**
   - Cachear usuários
   - Cachear históricos frequentes

2. **Paginação**
   - Histórico emocional paginado
   - Prevenir sobrecarga

3. **Otimização de Queries**
   - JOIN FETCH para evitar N+1
   - Índices no banco

### Melhorias de Segurança

1. **CORS Configurado**
2. **Rate Limiting**
3. **Auditoria de Entidades**
4. **HTTPS Enforced**
5. **Validação de Força de Senha**

### Novas Funcionalidades

1. **Análise Emocional**
   - Relatórios mensais
   - Tendências semanais
   - Recomendações personalizadas

2. **Notificações**
   - Lembretes diários
   - Alertas de padrões negativos

3. **Exportação de Dados**
   - PDF de relatórios
   - CSV do histórico

---

## 📝 NOTAS PARA O CV

### Tecnologias Demonstradas

Este projeto demonstra conhecimento prático em:

✅ **Backend:**
- Java 17 (features modernas)
- Spring Boot 3.x (última versão)
- Spring Data JPA (ORM)
- Spring Security (autenticação)
- RESTful API design

✅ **Banco de Dados:**
- MySQL
- Migrations com Flyway
- Modelagem relacional
- JPA Repositories

✅ **Boas Práticas:**
- Arquitetura em camadas
- DTOs e separação de responsabilidades
- Validação de dados
- Exclusão lógica (soft delete)
- Criptografia de senhas

✅ **Ferramentas:**
- Maven (build)
- Git (controle de versão)
- Lombok (produtividade)

### Competências Técnicas

- [x] Desenvolvimento de APIs REST
- [x] Autenticação e autorização
- [x] Persistência de dados com JPA
- [x] Validação de entrada
- [x] Migrations de banco de dados
- [x] Tratamento de relacionamentos (1:1, 1:N)
- [x] Lógica de negócio customizada

### Áreas de Melhoria Identificadas

**Demonstra autocrítica e visão de evolução:**

- [ ] Implementação de testes automatizados
- [ ] Refatoração arquitetural
- [ ] Documentação de API
- [ ] Containerização
- [ ] CI/CD
- [ ] Monitoramento e observabilidade

---

## 📚 RECURSOS E REFERÊNCIAS

### Documentação Oficial
- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security Docs](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Flyway Documentation](https://flywaydb.org/documentation/)

### Tutoriais Usados
- [Alura - Spring Boot](https://www.alura.com.br/curso-online-spring-boot)
- [Baeldung - Spring Security](https://www.baeldung.com/spring-security)
- [Spring Guides](https://spring.io/guides)

### Ferramentas de Desenvolvimento
- **IDE:** IntelliJ IDEA / Eclipse / VS Code
- **API Testing:** Postman / Insomnia
- **Database:** MySQL Workbench
- **Git UI:** GitKraken / SourceTree (opcional)

---

## 🏷️ TAGS E KEYWORDS

Para facilitar busca e indexação:

`#Java` `#SpringBoot` `#REST-API` `#MySQL` `#Spring-Security` `#JPA` `#Flyway` `#Maven` `#Backend` `#Mental-Health` `#Healthcare` `#Wellness` `#Emotional-Tracking` `#BCrypt` `#Authentication` `#CRUD` `#Layered-Architecture`

---

## 📞 CONTATO E CONTRIBUIÇÃO

**Desenvolvedor:** [Seu Nome]
**Email:** [seu.email@example.com]
**LinkedIn:** [linkedin.com/in/seu-perfil](https://linkedin.com/in/seu-perfil)
**GitHub:** [github.com/EversonRubira](https://github.com/EversonRubira)

**Repositório:** [GestaoSaudeMental](https://github.com/EversonRubira/GestaoSaudeMental)

---

## 📄 LICENÇA

[Especificar licença - MIT, Apache 2.0, etc.]

---

## 🎓 CONTEXTO ACADÊMICO/PROFISSIONAL

Este projeto foi desenvolvido como:
- [ ] Projeto acadêmico (TCC, disciplina, etc)
- [x] Projeto pessoal de portfólio
- [ ] Projeto profissional
- [ ] Contribuição open source

**Objetivo:** Demonstrar habilidades em desenvolvimento backend Java/Spring Boot para oportunidades profissionais.

---

**Última Atualização:** 17/11/2025
**Versão do Snapshot:** 1.0.0
**Status:** Projeto Base - Pronto para Melhorias
