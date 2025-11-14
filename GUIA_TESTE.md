# 🚀 Guia Completo de Teste - API Gestão de Saúde Mental

## 📋 Índice
1. [Pré-requisitos](#pré-requisitos)
2. [Configuração Inicial](#configuração-inicial)
3. [Iniciando a Aplicação](#iniciando-a-aplicação)
4. [Testando com Swagger UI](#testando-com-swagger-ui)
5. [Testando com cURL](#testando-com-curl)
6. [Testando com Postman](#testando-com-postman)
7. [Cenários de Teste](#cenários-de-teste)
8. [Troubleshooting](#troubleshooting)

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- ✅ **Java 17** ou superior
- ✅ **Maven 3.8+**
- ✅ **MySQL 8.0+**
- ✅ (Opcional) **Postman** ou **Insomnia**

### Verificar Instalações

```bash
# Verificar Java
java -version
# Saída esperada: openjdk version "17.x.x"

# Verificar Maven
mvn -version
# Saída esperada: Apache Maven 3.x.x

# Verificar MySQL
mysql --version
# Saída esperada: mysql Ver 8.x.x
```

---

## ⚙️ Configuração Inicial

### 1. Criar Banco de Dados MySQL

```bash
# Conectar ao MySQL
mysql -u root -p

# Criar banco de dados
CREATE DATABASE gestaosaudemental_api;

# Verificar criação
SHOW DATABASES;

# Sair
exit;
```

### 2. Configurar Credenciais (se necessário)

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestaosaudemental_api
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI  # ← Altere aqui

# JWT Secret (OPCIONAL - use a padrão para testes)
jwt.secret=${JWT_SECRET:gestao-saude-mental-secret-key-2024-change-in-production}
```

### 3. Instalar Dependências

```bash
# No diretório raiz do projeto
cd /home/user/GestaoSaudeMental

# Baixar dependências e compilar
mvn clean install
```

**Saída esperada:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 30.567 s
```

---

## 🎬 Iniciando a Aplicação

### Opção 1: Via Maven (Desenvolvimento)

```bash
mvn spring-boot:run
```

### Opção 2: Via JAR (Produção)

```bash
# Gerar JAR
mvn clean package -DskipTests

# Executar JAR
java -jar target/api-0.0.1-SNAPSHOT.jar
```

### ✅ Aplicação Rodando!

Você verá logs como:

```
2025-11-14 10:30:45.123  INFO --- [  restartedMain] g.api.ApiApplication    : Started ApiApplication in 5.234 seconds
2025-11-14 10:30:45.456  INFO --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http)
```

**URLs Importantes:**
- 🌐 API Base: `http://localhost:8080`
- 📖 Swagger UI: `http://localhost:8080/swagger-ui.html`
- 📄 OpenAPI Docs: `http://localhost:8080/v3/api-docs`

---

## 📖 Testando com Swagger UI

### 1. Abrir Swagger UI

Navegue para: **http://localhost:8080/swagger-ui.html**

Você verá uma interface com todos os endpoints:

```
┌─────────────────────────────────────────────────────────┐
│  API Gestão de Saúde Mental - v1.0                     │
├─────────────────────────────────────────────────────────┤
│  📂 Autenticação                                        │
│    POST /login - Realizar login                         │
│                                                          │
│  📂 Usuários                                            │
│    POST /usuarios - Cadastrar novo usuário              │
│    POST /usuarios/{id}/historico - Registrar emoção     │
│    GET  /usuarios/{id}/historico_por_periodo            │
│    GET  /usuarios/{id}/historico-cronologico            │
│    DELETE /usuarios/{id} - Desativar conta              │
└─────────────────────────────────────────────────────────┘
```

### 2. Testar Cadastro de Usuário

1. **Expandir** `POST /usuarios`
2. **Clicar** em "Try it out"
3. **Preencher** o JSON:

```json
{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "telefone": "11987654321",
  "dataNascimento": "1995-05-15",
  "genero": "FEMININO",
  "senha": "senha123"
}
```

4. **Clicar** em "Execute"

**Resposta esperada (201 Created):**
```json
{
  "id": 1,
  "nome": "Maria Silva"
}
```

### 3. Testar Login

1. **Expandir** `POST /login`
2. **Clicar** em "Try it out"
3. **Preencher**:

```json
{
  "login": "maria@email.com",
  "senha": "senha123"
}
```

4. **Clicar** em "Execute"

**Resposta esperada (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnZXN0YW8tc2F1ZGUtbWVudGFsLWFwaSIsInN1YiI6Im1hcmlhQGVtYWlsLmNvbSIsInVzZXJJZCI6MSwiZXhwIjoxNzMxNjA5NjAwfQ.abc123...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

### 4. Autenticar no Swagger

1. **Clicar** no botão **"Authorize"** (cadeado) no topo
2. **Colar** o token (apenas o valor, sem "Bearer ")
3. **Clicar** em "Authorize"
4. **Clicar** em "Close"

Agora você pode testar endpoints protegidos! 🔓

### 5. Registrar Estado Emocional

1. **Expandir** `POST /usuarios/{id}/historico`
2. **Clicar** em "Try it out"
3. **Preencher**:
   - `id`: `1`
   - Body:
   ```json
   {
     "estadoEmocional": "FELIZ",
     "atividadeRealizada": "EXERCICIO"
   }
   ```

4. **Clicar** em "Execute"

**Resposta esperada (201 Created):**
```
Registro de estado emocional criado com sucesso para o usuário com ID 1.
```

---

## 💻 Testando com cURL

### 1. Cadastrar Usuário

```bash
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Santos",
    "email": "joao@email.com",
    "telefone": "11999887766",
    "dataNascimento": "1988-10-20",
    "genero": "MASCULINO",
    "senha": "minhasenha"
  }'
```

**Resposta:**
```json
{"id":2,"nome":"João Santos"}
```

### 2. Fazer Login

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao@email.com",
    "senha": "minhasenha"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

**⚠️ IMPORTANTE:** Copie o token para usar nos próximos comandos!

### 3. Registrar Estado Emocional (com JWT)

```bash
# Substitua SEU_TOKEN_AQUI pelo token recebido no login
curl -X POST http://localhost:8080/usuarios/2/historico \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "estadoEmocional": "ANSIOSO",
    "atividadeRealizada": "MEDITAR"
  }'
```

### 4. Listar Histórico (últimos 30 dias)

```bash
curl -X GET http://localhost:8080/usuarios/2/historico_por_periodo \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta:**
```json
[
  {
    "nome": "João Santos",
    "dataRegistro": "2025-11-14",
    "estadoEmocional": "ANSIOSO",
    "atividadeRealizada": "MEDITAR",
    "mensagemMotivacional": "João Santos, a meditação é uma ótima maneira de acalmar a mente."
  }
]
```

### 5. Listar Histórico com Filtro de Data

```bash
curl -X GET "http://localhost:8080/usuarios/2/historico_por_periodo?inicio=2025-11-01&fim=2025-11-30" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### 6. Listar Histórico por Emoção

```bash
# Listar todos os registros
curl -X GET http://localhost:8080/usuarios/2/historico-cronologico \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"

# Filtrar por emoção específica
curl -X GET "http://localhost:8080/usuarios/2/historico-cronologico?emocao=FELIZ" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### 7. Desativar Conta

```bash
curl -X DELETE http://localhost:8080/usuarios/2 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

**Resposta:** `204 No Content` (sucesso)

---

## 📮 Testando com Postman

### 1. Importar Collection

Crie uma nova Collection no Postman chamada "Gestão Saúde Mental".

### 2. Configurar Variáveis de Ambiente

Crie um Environment com:

| Variável | Valor Inicial |
|----------|---------------|
| `base_url` | `http://localhost:8080` |
| `token` | (deixe vazio) |
| `user_id` | (deixe vazio) |

### 3. Criar Requests

#### Request 1: Cadastrar Usuário

- **Método:** POST
- **URL:** `{{base_url}}/usuarios`
- **Headers:**
  - `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "nome": "Ana Paula",
  "email": "ana@email.com",
  "telefone": "11988776655",
  "dataNascimento": "1992-03-08",
  "genero": "FEMININO",
  "senha": "ana123"
}
```

- **Tests (Script):**
```javascript
if (pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.environment.set("user_id", jsonData.id);
}
```

#### Request 2: Login

- **Método:** POST
- **URL:** `{{base_url}}/login`
- **Headers:**
  - `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "login": "ana@email.com",
  "senha": "ana123"
}
```

- **Tests (Script):**
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("token", jsonData.token);
}
```

#### Request 3: Registrar Emoção

- **Método:** POST
- **URL:** `{{base_url}}/usuarios/{{user_id}}/historico`
- **Headers:**
  - `Content-Type: application/json`
  - `Authorization: Bearer {{token}}`
- **Body (raw JSON):**
```json
{
  "estadoEmocional": "FELIZ",
  "atividadeRealizada": "SOCIALIZAR"
}
```

#### Request 4: Listar Histórico

- **Método:** GET
- **URL:** `{{base_url}}/usuarios/{{user_id}}/historico-cronologico`
- **Headers:**
  - `Authorization: Bearer {{token}}`

---

## 🧪 Cenários de Teste Completos

### Cenário 1: Fluxo Completo de Usuário

```bash
# 1. Cadastrar
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teste User","email":"teste@test.com","telefone":"11911112222","dataNascimento":"1990-01-01","genero":"OUTROS","senha":"test123"}'

# 2. Login (salve o token)
TOKEN=$(curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"login":"teste@test.com","senha":"test123"}' \
  | jq -r '.token')

echo "Token: $TOKEN"

# 3. Registrar 3 emoções diferentes
curl -X POST http://localhost:8080/usuarios/1/historico \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"estadoEmocional":"FELIZ","atividadeRealizada":"EXERCICIO"}'

curl -X POST http://localhost:8080/usuarios/1/historico \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"estadoEmocional":"CANSADO","atividadeRealizada":"TRABALHO"}'

curl -X POST http://localhost:8080/usuarios/1/historico \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"estadoEmocional":"ANSIOSO","atividadeRealizada":"MEDITAR"}'

# 4. Listar histórico
curl -X GET http://localhost:8080/usuarios/1/historico-cronologico \
  -H "Authorization: Bearer $TOKEN" | jq
```

### Cenário 2: Testar Validações

```bash
# Email inválido (deve retornar 400)
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teste","email":"invalido","telefone":"11911112222","dataNascimento":"1990-01-01","genero":"MASCULINO","senha":"test123"}'

# Email duplicado (deve retornar 409 Conflict)
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"Outro User","email":"teste@test.com","telefone":"11922223333","dataNascimento":"1990-01-01","genero":"FEMININO","senha":"test456"}'
```

### Cenário 3: Testar Autenticação

```bash
# Tentar acessar sem token (deve retornar 403)
curl -X GET http://localhost:8080/usuarios/1/historico-cronologico

# Token inválido (deve retornar 401)
curl -X GET http://localhost:8080/usuarios/1/historico-cronologico \
  -H "Authorization: Bearer token_invalido_123"

# Credenciais erradas (deve retornar 401)
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"login":"teste@test.com","senha":"senha_errada"}'
```

---

## 🎯 Checklist de Testes

Use este checklist para garantir que tudo está funcionando:

### ✅ Endpoints Públicos
- [ ] `POST /usuarios` - Cadastrar novo usuário
- [ ] `POST /login` - Login retorna token JWT
- [ ] `GET /hello` - Endpoint de teste responde

### ✅ Endpoints Protegidos (com JWT)
- [ ] `POST /usuarios/{id}/historico` - Registrar emoção
- [ ] `GET /usuarios/{id}/historico_por_periodo` - Listar por período
- [ ] `GET /usuarios/{id}/historico-cronologico` - Listar cronológico
- [ ] `DELETE /usuarios/{id}` - Desativar conta

### ✅ Validações
- [ ] Email inválido retorna 400
- [ ] Email duplicado retorna 409
- [ ] Data nascimento futura retorna 400
- [ ] Campos obrigatórios vazios retornam 400

### ✅ Autenticação
- [ ] Login sem credenciais retorna 401
- [ ] Login com senha errada retorna 401
- [ ] Endpoint protegido sem token retorna 403
- [ ] Endpoint protegido com token inválido retorna 401
- [ ] Endpoint protegido com token válido retorna 200

### ✅ Funcionalidades
- [ ] Mensagens motivacionais são geradas corretamente
- [ ] Filtro por data funciona
- [ ] Filtro por emoção funciona
- [ ] Exclusão lógica (ativo=false) funciona

### ✅ Swagger
- [ ] Swagger UI acessível em `/swagger-ui.html`
- [ ] Todos os endpoints aparecem
- [ ] Autenticação JWT funciona no Swagger

---

## 🔍 Troubleshooting

### Problema: Erro ao conectar ao MySQL

**Erro:**
```
Communications link failure
```

**Solução:**
1. Verifique se o MySQL está rodando:
```bash
sudo systemctl status mysql
# ou
sudo service mysql status
```

2. Inicie o MySQL se necessário:
```bash
sudo systemctl start mysql
```

3. Verifique credenciais no `application.properties`

### Problema: Porta 8080 já em uso

**Erro:**
```
Port 8080 is already in use
```

**Solução 1:** Matar processo na porta 8080
```bash
# Linux/Mac
lsof -i :8080
kill -9 PID_DO_PROCESSO

# Windows
netstat -ano | findstr :8080
taskkill /PID PID_DO_PROCESSO /F
```

**Solução 2:** Usar outra porta
Adicione em `application.properties`:
```properties
server.port=8081
```

### Problema: Flyway migration falhou

**Erro:**
```
FlywayException: Validate failed
```

**Solução:**
```bash
# Conectar ao MySQL
mysql -u root -p gestaosaudemental_api

# Limpar histórico Flyway
DELETE FROM flyway_schema_history;

# Ou recriar banco
DROP DATABASE gestaosaudemental_api;
CREATE DATABASE gestaosaudemental_api;

# Reiniciar aplicação
```

### Problema: Token JWT inválido

**Erro:**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Token JWT inválido ou expirado."
}
```

**Solução:**
1. Verifique se copiou o token completo
2. Certifique-se de usar o formato: `Bearer SEU_TOKEN`
3. Token expira em 24h - faça login novamente

### Problema: Dependências não baixam

**Erro:**
```
Could not resolve dependencies
```

**Solução:**
```bash
# Limpar cache Maven
mvn dependency:purge-local-repository

# Baixar novamente
mvn clean install -U
```

---

## 📊 Logs Úteis

Durante os testes, observe os logs da aplicação:

### Login bem-sucedido:
```
INFO  - Tentativa de login para o usuário: teste@test.com
INFO  - Token JWT gerado com sucesso para o usuário: teste@test.com
INFO  - Login realizado com sucesso para o usuário: teste@test.com
```

### Cadastro de usuário:
```
INFO  - Requisição de cadastro recebida para o email: teste@test.com
INFO  - Iniciando cadastro de usuário: teste@test.com
INFO  - Usuário criado com ID: 1
INFO  - Credenciais criadas para o usuário: teste@test.com
```

### Erro de autenticação:
```
ERROR - Credenciais inválidas
ERROR - Token JWT inválido ou expirado: Invalid signature
```

---

## 🎓 Valores Válidos

### Estados Emocionais
- `FELIZ`
- `TRISTE`
- `ANSIOSO`
- `CANSADO`
- `IRRITADO`
- `OUTROS`

### Atividades
- `EXERCICIO`
- `LEITURA`
- `TRABALHO`
- `SOCIALIZAR`
- `ASSISTIR_TV`
- `MEDITAR`
- `OUTROS`

### Gêneros
- `MASCULINO`
- `FEMININO`
- `NAO_BINARIO`
- `OUTROS`

---

## 📝 Próximos Passos

Após testar com sucesso, você pode:

1. ✅ Integrar com um frontend (React, Angular, Vue)
2. ✅ Adicionar mais endpoints (estatísticas, relatórios)
3. ✅ Implementar testes automatizados
4. ✅ Deploy em produção (Heroku, AWS, Railway)

---

## 💡 Dicas Finais

1. **Use variáveis de ambiente** para o JWT secret em produção:
```bash
export JWT_SECRET="sua-chave-super-secreta-aqui"
java -jar api.jar
```

2. **Ative o perfil de produção:**
```bash
java -jar api.jar --spring.profiles.active=prod
```

3. **Monitore os logs:**
```bash
tail -f logs/application.log
```

4. **Teste erros propositalmente** para ver o tratamento de exceções

---

**🎉 Parabéns! Sua API está pronta para uso!**

Para suporte, abra uma issue no repositório ou consulte a documentação no Swagger UI.
