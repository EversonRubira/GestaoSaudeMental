# Mental Health Management

Description
Mental Health Management is an app designed to help users improve their mental well-being. The system lets users record their emotional state daily and suggests actions based on these records. It also provides an emotional history so users can track their progress over time.

Current Features
User Registration: Allows new users to sign up with basic personal information.
Daily Emotional Recording: Users can log their current emotional state and activity.
Personal Emotional History: Shows the user’s emotional history for tracking trends over time.
Motivational Messages: Offers personalized suggestions based on the user’s emotional state and activity.
Logical User Deactivation: Users can be deactivated without losing their historical data.
API Endpoints
Users
POST /usuarios: Creates a new user.
PUT /usuarios: Updates a user’s personal information.
DELETE /usuarios/{id}: Deactivates a user logically.
Emotional History
POST /usuarios/{id}/historico: Records the user’s emotional state and activity.
GET /usuarios/{id}/historico_por_periodo: Shows the emotional history of a user within a specific time range.
GET /usuarios/{id}/historico-cronologico: Displays the user’s full emotional history in chronological order.
Technologies Used
Java 17
Spring Boot 3.3.2
JPA/Hibernate
MySQL (managed with Flyway)
Maven for dependencies
Planned Improvements
Emotional Trends: Add charts to show emotional trends over time (using Python/Matplotlib).
Reminders: Configurable reminders to log emotional states daily.
User Security: Add JWT authentication for endpoint protection.
Better Suggestions: Use external APIs for advanced motivational tips.
User Interface: Build an intuitive frontend.
Testing: Add unit and integration tests for reliability.
Deployment: Use Docker containers for easy deployment.
How to Contribute
Contributions are welcome! Here’s how you can help:

Fork this repository.
Create a branch for your feature or fix (git checkout -b my-feature).
Submit a pull request.


# GestaoSaudeMental

Descrição
O Gestão Saúde Mental é uma aplicação projetada para auxiliar os usuários a melhorar seu bem-estar mental. O sistema permite que os usuários registrem diariamente seu estado emocional e sugere ações baseadas nesses registros. Além disso, oferece um histórico emocional para que os usuários possam acompanhar sua evolução ao longo do tempo.

Funcionalidades Atuais
Cadastro de Usuários: Permite o registro de novos usuários com informações pessoais básicas.
Registro Diário de Estado Emocional: Os usuários podem registrar seu estado emocional atual e a atividade realizada.
Histórico Emocional Pessoal: Exibição do histórico emocional do usuário, permitindo a análise de tendências ao longo do tempo.
Sugestões Motivacionais Personalizadas: Com base no estado emocional e na atividade realizada, o sistema fornece mensagens motivacionais.
Exclusão Lógica de Usuários: Usuários podem ser desativados sem perder seus dados históricos.
Endpoints da API
Usuários
POST /usuarios: Cadastra um novo usuário.
PUT /usuarios: Atualiza as informações pessoais do usuário.
DELETE /usuarios/{id}: Realiza a exclusão lógica de um usuário.
Histórico Emocional
POST /usuarios/{id}/historico: Registra o estado emocional e a atividade realizada por um usuário.
GET /usuarios/{id}/historico_por_periodo: Exibe o histórico emocional de um usuário em um intervalo de tempo.
GET /usuarios/{id}/historico-cronologico: Lista o histórico emocional completo do usuário em ordem cronológica.
Tecnologias Utilizadas
Java 17
Spring Boot 3.3.2
JPA/Hibernate
MySQL (gerenciado com Flyway)
Maven para gerenciamento de dependências
Ajustes e Melhorias Planejados
Análise de Tendências Emocionais: Implementação de gráficos dinâmicos para visualização de tendências (usando Python/Matplotlib).
Notificações e Lembretes: Lembretes configuráveis para registro diário de estado emocional.
Autenticação e Autorização: Implementação de autenticação JWT para proteger os endpoints.
Sugestões Mais Avançadas: Integração com APIs externas para enriquecer as sugestões motivacionais.
Interface Gráfica: Desenvolvimento de um frontend intuitivo.
Testes Automatizados: Testes unitários e de integração para maior confiabilidade.
Deploy: Configuração de containers Docker para facilitar o deploy.
Como Contribuir
Contribuições são bem-vindas! Para colaborar:

Faça um fork deste repositório.
Crie uma branch para sua funcionalidade ou correção (git checkout -b minha-feature).
Envie suas alterações por meio de um pull request.


Com todos os arquivos agora carregados, vou estruturar a documentação final para o seu projeto. O resultado incluirá:

1. **Introdução**
   - Visão geral e objetivo da API.

2. **Configuração**
   - Dependências necessárias (exemplo: Spring Boot, MySQL).
   - Passos para rodar o projeto localmente.

3. **Arquitetura**
   - Estrutura básica do projeto.
   - Tecnologias e conceitos usados (JWT, autenticação, etc.).

4. **Endpoints**
   - Descrição detalhada de cada endpoint (autenticação, cadastro, histórico emocional, etc.), com exemplos de requisições e respostas.

5. **Modelo de Dados**
   - Estrutura das entidades (Usuario, Credenciais, etc.).
   - Enumerações (EstadoEmocional, Genero, etc.).

6. **Mensagens Motivacionais**
   - Lógica de mensagens baseadas no estado emocional.

7. **Configuração de Segurança**
   - Explicação da configuração do Spring Security.

8. **Scripts SQL**
   - Estrutura das tabelas no banco de dados e scripts utilizados.

Aguarde só um instante enquanto organizo tudo! 😄

Aqui está a **documentação completa** para o seu projeto! 🎉

---

# **Gestão Saúde Mental API - Documentação**

## **1. Introdução**
A API tem como objetivo facilitar o acompanhamento do bem-estar emocional de usuários, fornecendo ferramentas para registro, análise e motivação com base em estados emocionais.

---

## **2. Configuração**
### **2.1. Dependências**
- **Java 17+**
- **Spring Boot 3.x**
- **MySQL**
- **Flyway para migrações**

### **2.2. Configurações**
Arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestaosaudemental_api
spring.datasource.username=root
spring.datasource.password=root

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

### **2.3. Como executar**
1. Configure o banco MySQL.
2. Insira os scripts SQL na ordem (pasta `db/migration`).
3. Execute o projeto via sua IDE ou linha de comando (`mvn spring-boot:run`).

---

## **3. Arquitetura**
### **3.1. Tecnologias**
- **Spring Boot:** Estrutura principal.
- **Spring Security:** Autenticação JWT.
- **Flyway:** Gerenciamento de banco de dados.
- **Hibernate:** ORM para interações com banco.

---

## **4. Endpoints**
### **4.1. Autenticação**
- **POST** `/login`  
  **Descrição:** Autenticar o usuário e retornar o token.  
  **Body:** 
  ```json
  {
    "login": "email@example.com",
    "senha": "senha123"
  }
  ```
  **Resposta:** `200 OK`

---

### **4.2. Usuários**
#### **4.2.1. Cadastro de Usuário**
- **POST** `/usuarios`  
  **Descrição:** Cadastrar um novo usuário.  
  **Body:**
  ```json
  {
    "nome": "João Silva",
    "email": "joao@example.com",
    "telefone": "123456789",
    "dataNascimento": "1990-01-01",
    "genero": "MASCULINO",
    "senha": "senha123"
  }
  ```
  **Resposta:**
  ```json
  {
    "id": 1,
    "nome": "João Silva"
  }
  ```

#### **4.2.2. Registrar Estado Emocional**
- **POST** `/usuarios/{id}/historico`  
  **Body:**
  ```json
  {
    "estadoEmocional": "FELIZ",
    "atividadeRealizada": "EXERCICIO"
  }
  ```
  **Resposta:** `201 Created`

#### **4.2.3. Listar Histórico por Período**
- **GET** `/usuarios/{id}/historico_por_periodo?inicio=2024-01-01&fim=2024-12-31`  
  **Resposta:**
  ```json
  [
    {
      "nome": "João Silva",
      "dataRegistro": "2024-01-02",
      "estadoEmocional": "FELIZ",
      "atividadeRealizada": "EXERCICIO",
      "mensagemMotivacional": "João, mantenha essa energia positiva enquanto se exercita!"
    }
  ]
  ```

---

## **5. Modelo de Dados**
### **5.1. Entidades**
- **Usuario**:
  - ID, nome, email, telefone, data de nascimento, estado emocional, atividade realizada.
- **HistoricoEmocional**:
  - ID, dataRegistro, estadoEmocional, atividadeRealizada, usuario.

### **5.2. Enumerações**
- **EstadoEmocionalEnum**: `FELIZ`, `TRISTE`, `ANSIOSO`, etc.
- **AtividadeRealizadaEnum**: `EXERCICIO`, `LEITURA`, `TRABALHO`, etc.
- **GeneroEnum**: `MASCULINO`, `FEMININO`, etc.

---

## **6. Mensagens Motivacionais**
Lógica baseada em `MensagemMotivacional`:
- Exemplo para estado **FELIZ** e atividade **EXERCICIO**:
  ```java
  "João, mantenha essa energia positiva enquanto se exercita!"
  ```

---

## **7. Segurança**
Configuração no arquivo `SecurityConfiguration.java`:
- Rotas públicas: `/login`.
- Rotas protegidas: `/usuarios/**`.

---

## **8. Scripts SQL**
### **8.1. Estrutura das Tabelas**
- Arquivo: `V1__create-table-usuarios.sql`
```sql
CREATE TABLE usuarios (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  telefone VARCHAR(15),
  data_nascimento DATE,
  ativo BOOLEAN DEFAULT TRUE
);
```

- Arquivo: `V5__create-table-credenciais.sql`
```sql
CREATE TABLE credenciais (
  id BIGINT PRIMARY KEY,
  senha VARCHAR(255) NOT NULL
);
```

---

## **9. Contribuição**
- Feedbacks e melhorias são bem-vindos! Abra uma PR ou envie um e-mail. 😄

---

Se precisar de mais ajustes ou detalhes, é só avisar! 🚀
