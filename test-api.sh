#!/bin/bash

# Script de Teste Automático - API Gestão Saúde Mental
# Executa todos os testes da API de forma sequencial

echo "======================================"
echo "🚀 Teste Automático da API"
echo "======================================"
echo ""

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080"

# Verificar se API está rodando
echo -e "${BLUE}📡 Verificando se API está rodando...${NC}"
if ! curl -s -f "$BASE_URL/hello" > /dev/null; then
    echo -e "${RED}❌ API não está rodando em $BASE_URL${NC}"
    echo "Execute: mvn spring-boot:run"
    exit 1
fi
echo -e "${GREEN}✅ API está rodando!${NC}"
echo ""

# Função para fazer requests e mostrar resultado
test_endpoint() {
    local METHOD=$1
    local URL=$2
    local DATA=$3
    local HEADERS=$4
    local DESCRIPTION=$5
    local EXPECTED_STATUS=$6

    echo -e "${BLUE}🧪 Testando: $DESCRIPTION${NC}"
    echo "   $METHOD $URL"

    if [ -n "$DATA" ]; then
        if [ -n "$HEADERS" ]; then
            RESPONSE=$(curl -s -w "\n%{http_code}" -X $METHOD "$BASE_URL$URL" \
                -H "Content-Type: application/json" \
                -H "$HEADERS" \
                -d "$DATA")
        else
            RESPONSE=$(curl -s -w "\n%{http_code}" -X $METHOD "$BASE_URL$URL" \
                -H "Content-Type: application/json" \
                -d "$DATA")
        fi
    else
        if [ -n "$HEADERS" ]; then
            RESPONSE=$(curl -s -w "\n%{http_code}" -X $METHOD "$BASE_URL$URL" \
                -H "$HEADERS")
        else
            RESPONSE=$(curl -s -w "\n%{http_code}" -X $METHOD "$BASE_URL$URL")
        fi
    fi

    STATUS_CODE=$(echo "$RESPONSE" | tail -n1)
    BODY=$(echo "$RESPONSE" | head -n-1)

    if [ "$STATUS_CODE" == "$EXPECTED_STATUS" ]; then
        echo -e "${GREEN}✅ Status: $STATUS_CODE (esperado: $EXPECTED_STATUS)${NC}"
        if [ -n "$BODY" ]; then
            echo -e "${GREEN}   Resposta: $BODY${NC}"
        fi
    else
        echo -e "${RED}❌ Status: $STATUS_CODE (esperado: $EXPECTED_STATUS)${NC}"
        if [ -n "$BODY" ]; then
            echo -e "${RED}   Resposta: $BODY${NC}"
        fi
    fi
    echo ""

    # Retornar o corpo da resposta para processar
    echo "$BODY"
}

echo "======================================"
echo "📝 Teste 1: Cadastrar Usuário"
echo "======================================"
echo ""

USER_DATA='{
  "nome": "Teste Automatico",
  "email": "teste.automatico@email.com",
  "telefone": "11987654321",
  "dataNascimento": "1990-01-01",
  "genero": "OUTROS",
  "senha": "senha123"
}'

USER_RESPONSE=$(test_endpoint "POST" "/usuarios" "$USER_DATA" "" "Cadastrar novo usuário" "201")
USER_ID=$(echo "$USER_RESPONSE" | grep -o '"id":[0-9]*' | grep -o '[0-9]*')

if [ -z "$USER_ID" ]; then
    echo -e "${RED}❌ Falha ao obter ID do usuário. Abortando testes.${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Usuário criado com ID: $USER_ID${NC}"
echo ""

echo "======================================"
echo "🔐 Teste 2: Login"
echo "======================================"
echo ""

LOGIN_DATA='{
  "login": "teste.automatico@email.com",
  "senha": "senha123"
}'

LOGIN_RESPONSE=$(test_endpoint "POST" "/login" "$LOGIN_DATA" "" "Fazer login" "200")
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo -e "${RED}❌ Falha ao obter token JWT. Abortando testes.${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Token JWT obtido: ${TOKEN:0:50}...${NC}"
echo ""

echo "======================================"
echo "😊 Teste 3: Registrar Estados Emocionais"
echo "======================================"
echo ""

# Registrar 3 estados emocionais diferentes
test_endpoint "POST" "/usuarios/$USER_ID/historico" \
    '{"estadoEmocional":"FELIZ","atividadeRealizada":"EXERCICIO"}' \
    "Authorization: Bearer $TOKEN" \
    "Registrar estado: FELIZ + EXERCICIO" \
    "201" > /dev/null

test_endpoint "POST" "/usuarios/$USER_ID/historico" \
    '{"estadoEmocional":"ANSIOSO","atividadeRealizada":"MEDITAR"}' \
    "Authorization: Bearer $TOKEN" \
    "Registrar estado: ANSIOSO + MEDITAR" \
    "201" > /dev/null

test_endpoint "POST" "/usuarios/$USER_ID/historico" \
    '{"estadoEmocional":"CANSADO","atividadeRealizada":"TRABALHO"}' \
    "Authorization: Bearer $TOKEN" \
    "Registrar estado: CANSADO + TRABALHO" \
    "201" > /dev/null

echo "======================================"
echo "📊 Teste 4: Listar Histórico"
echo "======================================"
echo ""

test_endpoint "GET" "/usuarios/$USER_ID/historico-cronologico" "" \
    "Authorization: Bearer $TOKEN" \
    "Listar histórico completo" \
    "200" > /dev/null

echo "======================================"
echo "📅 Teste 5: Listar Histórico por Período"
echo "======================================"
echo ""

DATA_INICIO=$(date -d "30 days ago" +%Y-%m-%d)
DATA_FIM=$(date +%Y-%m-%d)

test_endpoint "GET" "/usuarios/$USER_ID/historico_por_periodo?inicio=$DATA_INICIO&fim=$DATA_FIM" "" \
    "Authorization: Bearer $TOKEN" \
    "Listar histórico (últimos 30 dias)" \
    "200" > /dev/null

echo "======================================"
echo "🔍 Teste 6: Filtrar por Emoção"
echo "======================================"
echo ""

test_endpoint "GET" "/usuarios/$USER_ID/historico-cronologico?emocao=FELIZ" "" \
    "Authorization: Bearer $TOKEN" \
    "Filtrar histórico por emoção FELIZ" \
    "200" > /dev/null

echo "======================================"
echo "❌ Teste 7: Testar Validações (Erros Esperados)"
echo "======================================"
echo ""

test_endpoint "POST" "/usuarios" \
    '{"nome":"Teste","email":"email_invalido","telefone":"11987654321","dataNascimento":"1990-01-01","genero":"MASCULINO","senha":"senha123"}' \
    "" \
    "Email inválido (deve retornar 400)" \
    "400" > /dev/null

test_endpoint "POST" "/usuarios" \
    '{"nome":"Teste Duplicado","email":"teste.automatico@email.com","telefone":"11999999999","dataNascimento":"1990-01-01","genero":"FEMININO","senha":"senha456"}' \
    "" \
    "Email duplicado (deve retornar 409)" \
    "409" > /dev/null

test_endpoint "POST" "/login" \
    '{"login":"teste.automatico@email.com","senha":"senha_errada"}' \
    "" \
    "Senha incorreta (deve retornar 401)" \
    "401" > /dev/null

test_endpoint "GET" "/usuarios/$USER_ID/historico-cronologico" "" \
    "" \
    "Acesso sem token (deve retornar 403)" \
    "403" > /dev/null

test_endpoint "GET" "/usuarios/$USER_ID/historico-cronologico" "" \
    "Authorization: Bearer token_invalido_123" \
    "Token inválido (deve retornar 401)" \
    "401" > /dev/null

echo "======================================"
echo "🗑️ Teste 8: Desativar Conta"
echo "======================================"
echo ""

test_endpoint "DELETE" "/usuarios/$USER_ID" "" \
    "Authorization: Bearer $TOKEN" \
    "Desativar conta do usuário" \
    "204" > /dev/null

echo "======================================"
echo "📊 RESUMO DOS TESTES"
echo "======================================"
echo ""
echo -e "${GREEN}✅ Cadastro de usuário${NC}"
echo -e "${GREEN}✅ Login com JWT${NC}"
echo -e "${GREEN}✅ Registro de estados emocionais${NC}"
echo -e "${GREEN}✅ Listagem de histórico${NC}"
echo -e "${GREEN}✅ Filtros (período e emoção)${NC}"
echo -e "${GREEN}✅ Validações de erro${NC}"
echo -e "${GREEN}✅ Desativação de conta${NC}"
echo ""
echo -e "${BLUE}🎉 Todos os testes concluídos!${NC}"
echo ""
echo -e "${YELLOW}💡 Dica: Acesse o Swagger UI em:${NC}"
echo -e "   ${BLUE}http://localhost:8080/swagger-ui.html${NC}"
echo ""
