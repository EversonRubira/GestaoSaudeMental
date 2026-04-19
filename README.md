# GestaoSaudeMental API

API REST para acompanhamento de saúde mental. Permite que usuários registrem seu estado emocional diariamente e recebam mensagens motivacionais com base em seus registros e padrões emocionais.

---

## Funcionalidades

- **Cadastro de usuários** com informações pessoais básicas
- **Registro diário de estado emocional** e atividade realizada
- **Histórico emocional** consultável por período ou em ordem cronológica
- **Mensagens motivacionais personalizadas** com base no estado emocional e atividade
- **Desativação lógica** de usuários sem perda de dados históricos

---

## Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.3.4 |
| Persistência | JPA / Hibernate |
| Banco de dados | MySQL |
| Migrations | Flyway |
| Segurança | Spring Security + BCrypt + JWT |
| Build | Maven |

---

## Como rodar localmente

### Pré-requisitos

- Java 17+
- MySQL rodando em `localhost:3306`
- Maven

### Configuração

1. Clone o repositório
2. Crie o banco de dados:
   ```sql
   CREATE DATABASE gestaosaudemental_api;
   ```
3. Ajuste as credenciais em `src/main/resources/application.properties` se necessário
4. Execute:
   ```bash
   mvn spring-boot:run
   ```

As migrations do Flyway criam as tabelas automaticamente na primeira execução.

---

## Endpoints

### Autenticação

**POST** `/login`
```json
// Request
{ "login": "email@example.com", "senha": "senha123" }

// Response 200 OK
{ "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." }
```

> Todas as rotas abaixo exigem o header `Authorization: Bearer <token>`

---

### Usuários

**POST** `/usuarios` — Cadastrar novo usuário
```json
// Request
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "telefone": "11987654321",
  "dataNascimento": "1990-01-01",
  "genero": "MASCULINO",
  "senha": "senha123"
}

// Response 201 Created
{ "id": 1, "nome": "João Silva" }
```

**DELETE** `/usuarios/{id}` — Desativa o usuário logicamente → `204 No Content`

---

### Histórico Emocional

**POST** `/usuarios/{id}/historico` — Registrar estado emocional
```json
// Request
{ "estadoEmocional": "FELIZ", "atividadeRealizada": "EXERCICIO" }

// Response 201 Created
{
  "id": 1,
  "mensagemMotivacional": "João, mantenha essa energia positiva enquanto se exercita!"
}
```

**GET** `/usuarios/{id}/historico_por_periodo?inicio=2024-01-01&fim=2024-12-31`
```json
// Response 200 OK
[
  {
    "dataRegistro": "2024-01-02",
    "estadoEmocional": "FELIZ",
    "atividadeRealizada": "EXERCICIO"
  }
]
```

**GET** `/usuarios/{id}/historico-cronologico?emocao=FELIZ` — Histórico completo (filtro de emoção opcional)

---

## Estrutura do Banco de Dados

```
usuarios
  └── historico_emocional  (1 usuário → N registros)
  └── credenciais          (1 usuário → 1 credencial)
```

**Estados emocionais:** `FELIZ`, `TRISTE`, `ANSIOSO`, `CANSADO`, `IRRITADO`, `OUTROS`

**Atividades:** `EXERCICIO`, `LEITURA`, `TRABALHO`, `SOCIALIZAR`, `ASSISTIR_TV`, `MEDITAR`, `OUTROS`

**Gêneros:** `MASCULINO`, `FEMININO`, `NAO_BINARIO`, `OUTROS`

---

## Em desenvolvimento

- **Integração com API de Inteligência Artificial** — as mensagens motivacionais serão geradas por um modelo de IA, tornando as respostas muito mais contextuais, empáticas e personalizadas com base no histórico emocional do usuário
- Testes unitários e de integração
- Documentação automática com Swagger / OpenAPI
- Containerização com Docker

---

## Como contribuir

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b minha-feature`)
3. Commit suas alterações (`git commit -m 'feat: minha feature'`)
4. Envie um pull request
