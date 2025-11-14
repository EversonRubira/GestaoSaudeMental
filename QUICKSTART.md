# 🚀 Quick Start - Gestão Saúde Mental API

## ⚡ Início Rápido em 3 Minutos

### 1. Criar Banco de Dados (30 segundos)

```bash
mysql -u root -p -e "CREATE DATABASE gestaosaudemental_api;"
```

### 2. Iniciar Aplicação (2 minutos)

```bash
cd /home/user/GestaoSaudeMental
mvn spring-boot:run
```

Aguarde até ver:
```
Started ApiApplication in X.XXX seconds
```

### 3. Testar API (30 segundos)

**Opção A: Teste Automático**
```bash
./test-api.sh
```

**Opção B: Swagger UI**

Abra no navegador: **http://localhost:8080/swagger-ui.html**

**Opção C: Teste Manual Rápido**

```bash
# 1. Cadastrar
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"Teste","email":"teste@test.com","telefone":"11999999999","dataNascimento":"1990-01-01","genero":"OUTROS","senha":"test123"}'

# 2. Login (copie o token)
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"login":"teste@test.com","senha":"test123"}'

# 3. Usar API (substitua SEU_TOKEN)
curl -X POST http://localhost:8080/usuarios/1/historico \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN" \
  -d '{"estadoEmocional":"FELIZ","atividadeRealizada":"EXERCICIO"}'
```

---

## 📚 Documentação Completa

- **Guia Detalhado:** [GUIA_TESTE.md](./GUIA_TESTE.md)
- **Collection Postman:** [Gestao_Saude_Mental.postman_collection.json](./Gestao_Saude_Mental.postman_collection.json)
- **README Principal:** [README.md](./README.md)

---

## 🔗 URLs Importantes

| Recurso | URL |
|---------|-----|
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs |
| **Health Check** | http://localhost:8080/hello |

---

## 📋 Checklist de Teste Rápido

Execute estes comandos em ordem:

```bash
# ✅ 1. API está rodando?
curl http://localhost:8080/hello

# ✅ 2. Cadastrar usuário
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria","email":"maria@test.com","telefone":"11987654321","dataNascimento":"1995-01-01","genero":"FEMININO","senha":"maria123"}'

# ✅ 3. Fazer login e salvar token
TOKEN=$(curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"login":"maria@test.com","senha":"maria123"}' \
  | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

echo "Token: $TOKEN"

# ✅ 4. Registrar emoção
curl -X POST http://localhost:8080/usuarios/1/historico \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"estadoEmocional":"FELIZ","atividadeRealizada":"EXERCICIO"}'

# ✅ 5. Listar histórico
curl -X GET http://localhost:8080/usuarios/1/historico-cronologico \
  -H "Authorization: Bearer $TOKEN" | jq
```

Se todos os comandos funcionarem, sua API está 100% operacional! 🎉

---

## 🎯 Próximos Passos

1. ✅ **Testar no Swagger UI** - Interface visual completa
2. ✅ **Importar Collection no Postman** - Testes organizados
3. ✅ **Ler o GUIA_TESTE.md** - Documentação completa
4. ✅ **Integrar com Frontend** - React, Angular, Vue, etc.

---

## 🆘 Problemas Comuns

### API não inicia

```bash
# Verificar se MySQL está rodando
sudo systemctl status mysql

# Iniciar MySQL
sudo systemctl start mysql
```

### Porta 8080 ocupada

```bash
# Ver processo na porta
lsof -i :8080

# Matar processo
kill -9 PID
```

### Erro de dependências

```bash
mvn clean install -U
```

---

## 📞 Suporte

- **Issues:** https://github.com/EversonRubira/GestaoSaudeMental/issues
- **Documentação:** Ver arquivos `.md` na raiz do projeto
- **Swagger:** http://localhost:8080/swagger-ui.html

---

**Pronto! Sua API está funcionando! 🚀**
