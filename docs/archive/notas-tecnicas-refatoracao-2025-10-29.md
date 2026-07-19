> ⚠️ ARQUIVO HISTÓRICO — análise de 2025-10-29, desatualizada.
> A recomendação de migrar para Python/Node foi avaliada e descartada
> (ver decisoes-arquiteturais.md). Seção de bugs críticos foi usada
> como checklist em investigacao-branches.md.

# Notas Técnicas - Refatoração GestaoSaudeMental

**Data da Análise:** 2025-10-29
**Status do Projeto:** Em desenvolvimento - necessita refatoração

---

## 1. PROBLEMAS CRÍTICOS IDENTIFICADOS

### 1.1 Segurança Incompleta 🔴 URGENTE
**Localização:** `api/src/main/java/gestaoSaudeMental/api/infra/security/SecurityConfiguration.java:20`

**Problema:**
- Endpoints `/usuarios/**` exigem autenticação mas JWT não está implementado
- `AutenticacaoController:26` apenas valida credenciais, não retorna token
- Não há `UserDetailsService` configurado para buscar usuário do banco
- **Status atual:** Aplicação provavelmente não funciona ou segurança está desabilitada

**Solução:**
```java
// TODO: Implementar geração de JWT no AutenticacaoController
// TODO: Criar JwtTokenService para gerar/validar tokens
// TODO: Criar SecurityFilter para interceptar requests
// TODO: Implementar UserDetailsService buscando de Credenciais
```

### 1.2 Bug no Endpoint DELETE 🔴
**Localização:** `api/src/main/java/gestaoSaudeMental/api/controller/UsuarioController.java:124`

**Problema:**
```java
@DeleteMapping  // ❌ Falta /{id} aqui
public String excluirConta(@PathVariable Long id) // Espera {id} na URL
```

**Solução:**
```java
@DeleteMapping("/{id}")  // ✅ Corrigir
```

### 1.3 Campos Redundantes na Entidade Usuario
**Localização:** `api/src/main/java/gestaoSaudeMental/api/domain/usuario/Usuario.java:28-30`

**Problema:**
```java
private EstadoEmocionalEnum estadoEmocional;       // ❌ Nunca usado
private AtividadeRealizadaEnum atividadeRealizada; // ❌ Nunca usado
```
Esses dados já estão em `HistoricoEmocional`. Duplicação desnecessária.

**Solução:**
- Remover esses campos de Usuario
- Criar migration para dropar as colunas do banco
- Garantir que toda lógica usa apenas HistoricoEmocional

### 1.4 Tratamento de Erros Inexistente
**Problema:**
- Apenas `RuntimeException` genérica em varios lugares
- Sem `@ControllerAdvice` para handlers globais
- Cliente recebe stack traces ao invés de mensagens estruturadas

**Solução:**
```java
// TODO: Criar @ControllerAdvice com handlers para:
// - EntityNotFoundException
// - ValidationException
// - AuthenticationException
// - Retornar ResponseEntity com estrutura padronizada
```

### 1.5 Violações de Princípios REST
**Problemas:**
- Controllers retornam `String` ao invés de objetos estruturados
- Falta de códigos HTTP apropriados
- POST não retorna recurso criado com Location header

**Solução:**
- Refatorar todos endpoints para retornar `ResponseEntity<T>`
- Usar códigos HTTP corretos (201 Created, 204 No Content, etc)
- Seguir HATEOAS quando fizer sentido

### 1.6 Falta de Validação
**Problemas:**
- Email validation comentada: `Usuario.java:21` `//@Email`
- DTOs sem constraints `@NotNull`, `@Size`, etc
- Data de nascimento pode ser futura
- Sem validação de período em queries de histórico

**Solução:**
```java
// TODO: Adicionar Bean Validation em todos DTOs
@NotBlank(message = "Nome é obrigatório")
@Email(message = "Email inválido")
@Past(message = "Data de nascimento deve ser no passado")
```

---

## 2. ANÁLISE DE TECNOLOGIA

### 2.1 Java/Spring Boot é adequado?

**Resposta: NÃO para este projeto específico**

#### Quando Java/Spring Boot faz sentido:
- ✅ Aplicações enterprise com alta complexidade
- ✅ Sistemas com tráfego massivo (milhões de requests)
- ✅ Ambientes corporativos com infraestrutura Java
- ✅ Times grandes (tipagem forte = contratos claros)

#### Para este projeto:
- ❌ CRUD simples, lógica de negócio mínima
- ❌ Overkill técnico (configuração excessiva)
- ❌ Produtividade menor (compilação, restart lento)
- ❌ Recursos desperdiçados (JVM ~500MB vs ~50MB Node/Python)
- ❌ Ecossistema de IA limitado

### 2.2 Alternativas Recomendadas

#### 🥇 Opção 1: Node.js + Express/Fastify
**Vantagens:**
- Desenvolvimento 3-4x mais rápido
- JSON nativo (não precisa mapear)
- Ecossistema rico para IA (OpenAI SDK, LangChain)
- Serverless-friendly (Vercel, AWS Lambda)
- Hot reload instantâneo

**Exemplo:**
```javascript
// Mesma feature em ~15 linhas vs ~60 em Java
app.post('/usuarios/:id/historico', async (req, res) => {
  const historico = await HistoricoEmocional.create({
    usuarioId: req.params.id,
    ...req.body,
    dataRegistro: new Date()
  });

  const mensagem = await gerarMensagemIA(historico);
  res.status(201).json({ historico, mensagem });
});
```

**Stack sugerida:**
- **Framework:** Express.js ou Fastify
- **ORM:** Prisma ou TypeORM
- **Validação:** Zod ou Joi
- **Auth:** Passport.js + JWT
- **IA:** OpenAI SDK, LangChain
- **Database:** PostgreSQL (melhor que MySQL para JSON)

#### 🥇 Opção 2: Python + FastAPI (MELHOR PARA IA)
**Vantagens:**
- 🎯 **MELHOR para IA/ML** (NumPy, Pandas, scikit-learn, Transformers)
- Type hints modernos (menos verboso que Java)
- FastAPI gera documentação automática (Swagger)
- Notebooks para análise exploratória de dados
- Performance próxima de Node (async/await nativo)

**Exemplo:**
```python
@app.post("/usuarios/{id}/historico", status_code=201)
async def registrar_estado(
    id: int,
    dados: EstadoEmocionalDTO,
    db: Session = Depends(get_db)
):
    historico = await db.historico.create(usuario_id=id, **dados.dict())

    # IA nativa
    analise = await analisar_padrao_emocional(id)
    sugestao = await gerar_sugestao_ia(historico, analise)

    return {
        "historico": historico,
        "analise": analise,
        "sugestao": sugestao
    }
```

**Stack sugerida:**
- **Framework:** FastAPI
- **ORM:** SQLAlchemy 2.0 ou Tortoise ORM
- **Validação:** Pydantic (built-in FastAPI)
- **Auth:** FastAPI Security + JWT
- **IA:** OpenAI, Anthropic, Hugging Face Transformers
- **ML:** scikit-learn, pandas, numpy
- **Database:** PostgreSQL

#### Opção 3: Manter Java MAS...
Se decidir manter Java (por questão de aprendizado):
- ✅ Adicionar camada de Service (Controllers estão fazendo tudo)
- ✅ Implementar testes (atualmente 0 testes)
- ✅ Corrigir todos os bugs críticos acima
- ✅ Adicionar Swagger/OpenAPI
- ⚠️ Aceitar que integração com IA será mais trabalhosa

---

## 3. INTEGRAÇÃO COM IA

### 3.1 O que é Viável e Ético

#### ✅ RECOMENDADO: Análise de Padrões

**Features:**
```python
def analisar_tendencia_emocional(usuario_id, dias=30):
    historico = get_historico(usuario_id, ultimos_dias=dias)

    features = {
        # Métricas básicas
        'dias_negativos': count(estados in ['TRISTE', 'ANSIOSO', 'ESTRESSADO']),
        'dias_positivos': count(estados in ['FELIZ', 'CALMO']),
        'variacao': std_deviation(estados),

        # Tendências
        'melhora_semanal': media_ultima_semana > media_semana_anterior,
        'piora_acentuada': queda_rapida_ultimos_7_dias,

        # Correlações
        'atividade_fisica_freq': count('EXERCICIO_FISICO'),
        'correlacao_exercicio_humor': pearson(exercicio, humor_positivo),
        'horas_sono_media': media('DORMIR'),

        # Padrões temporais
        'dia_semana_pior': modo(dia_semana where estado_negativo),
        'consistencia_registros': len(historico) / dias
    }

    # ML simples mas efetivo
    nivel_alerta = classificar_risco(features)  # baixo/medio/alto

    return {
        'tendencia': 'melhorando' | 'estável' | 'piorando',
        'nivel_alerta': nivel_alerta,
        'insights': gerar_insights(features),
        'recomendacoes': gerar_recomendacoes(features)
    }
```

#### ✅ RECOMENDADO: Mensagens com LLM

**Integração OpenAI/Anthropic:**
```javascript
async function gerarMensagemPersonalizada(usuario, historico, estadoAtual) {
  const prompt = `
Contexto do usuário:
- Nome: ${usuario.nome}
- Idade: ${calcularIdade(usuario.dataNascimento)} anos
- Histórico recente (últimos 7 dias): ${historico.map(d => d.estado).join(', ')}
- Estado atual: ${estadoAtual.estado}
- Atividade realizada: ${estadoAtual.atividade}

Tarefa: Gere uma mensagem empática e construtiva (máximo 2 frases).
- Seja específico à situação
- Evite clichês genéricos
- Não faça diagnósticos ou prescreva tratamentos
- Foque em validação emocional e sugestões práticas
`;

  const response = await openai.chat.completions.create({
    model: "gpt-4",
    messages: [
      {
        role: "system",
        content: "Você é um assistente de bem-estar mental empático. Suas mensagens apoiam mas não substituem profissionais de saúde mental."
      },
      {role: "user", content: prompt}
    ],
    max_tokens: 150,
    temperature: 0.7
  });

  return response.choices[0].message.content;
}
```

**Alternativa com Anthropic Claude:**
```javascript
// Claude é mais cauteloso e empático para saúde mental
const message = await anthropic.messages.create({
  model: "claude-3-5-sonnet-20241022",
  max_tokens: 150,
  messages: [{
    role: "user",
    content: prompt
  }],
  system: "Você é um assistente empático de bem-estar mental..."
});
```

#### ✅ RECOMENDADO: Insights Semanais Automatizados

**Endpoint sugerido:**
```
GET /usuarios/{id}/insights-semanais

Response:
{
  "periodo": "2025-10-22 a 2025-10-29",
  "resumo": {
    "registros": 6,
    "estado_predominante": "neutro",
    "tendencia": "estável",
    "melhor_dia": "2025-10-24 (após exercício)",
    "pior_dia": "2025-10-27"
  },
  "correlacoes_positivas": [
    {
      "atividade": "EXERCICIO_FISICO",
      "impacto": "Você registrou humor melhor em 80% dos dias com exercício"
    },
    {
      "atividade": "MEDITACAO",
      "impacto": "Correlação positiva com estados calmos"
    }
  ],
  "sugestoes_ia": [
    "Notamos que exercícios físicos têm impacto positivo no seu humor. Que tal manter essa rotina?",
    "Sua consistência em registrar é excelente para autoconhecimento!"
  ],
  "alerta": null,
  "disclaimer": "Estas são observações baseadas em seus registros, não substituem acompanhamento profissional.",
  "recursos_profissionais": {
    "emergencia": "CVV: 188 (24h, gratuito)",
    "caps": "Encontre o CAPS mais próximo: saude.gov.br/caps",
    "terapia_online": ["Zenklub", "Vittude", "Psicologia Viva"]
  }
}
```

#### ⚠️ USAR COM CUIDADO: Detecção de Crises

**Sistema de alerta responsável:**
```python
def verificar_sinais_alerta(usuario_id):
    historico = get_historico(usuario_id, ultimos_dias=14)

    sinais_preocupantes = {
        'dias_consecutivos_negativos': count_consecutivos(['TRISTE', 'ANSIOSO', 'DEPRIMIDO']),
        'piora_acentuada': comparar_ultimas_2_semanas(),
        'falta_atividades_positivas': count('EXERCICIO' or 'SOCIAL') == 0,
        'registros_irregulares': len(historico) < 4  # usuário sumiu?
    }

    # Thresholds conservadores (evitar falsos positivos)
    if sinais_preocupantes['dias_consecutivos_negativos'] >= 10:
        return {
            'nivel': 'ATENCAO',
            'mensagem': 'Notamos que você tem passado por um período desafiador.',
            'recomendacao': 'Considere conversar com um profissional de saúde mental.',
            'recursos': get_recursos_profissionais(),
            'nao_urgente': True  # Não alarmar desnecessariamente
        }

    # Caso MUITO grave (uso de palavras-chave) - requer campo de notas
    # NÃO implementar sem suporte psicológico profissional disponível

    return None  # Tudo bem
```

### 3.2 APIs e Modelos Recomendados

#### Opção 1: OpenAI (Pago, Alta Qualidade)
```bash
# Custo estimado
# Mensagens curtas (100 tokens): ~$0.03 por 1000 interações
# Análise de histórico (500 tokens): ~$0.15 por 1000 análises
```

**Modelos:**
- `gpt-4-turbo`: Melhor qualidade, mais caro
- `gpt-3.5-turbo`: Bom custo-benefício para mensagens simples

#### Opção 2: Anthropic Claude (Pago, Mais Empático)
```bash
# Similar em preço ao OpenAI
# Melhor para contextos longos (200k tokens)
```

**Modelos:**
- `claude-3-5-sonnet`: Equilíbrio qualidade/custo
- `claude-3-opus`: Máxima qualidade (mais caro)

#### Opção 3: Hugging Face (Gratuito, Self-Hosted)
```python
# Modelos de análise de sentimento em português
from transformers import pipeline

# Sentimento
sentiment_analyzer = pipeline(
    "sentiment-analysis",
    model="neuralmind/bert-base-portuguese-cased"
)

# Classificação de emoções
emotion_classifier = pipeline(
    "text-classification",
    model="citizenlab/twitter-xlm-roberta-base-sentiment-finetunned"
)

resultado = sentiment_analyzer("Hoje foi um dia difícil, mas consegui fazer exercício")
# {'label': 'NEUTRAL', 'score': 0.73}
```

**Vantagens:**
- ✅ Gratuito
- ✅ Roda localmente (privacidade total)
- ✅ Sem limites de uso

**Desvantagens:**
- ❌ Qualidade inferior a GPT-4/Claude
- ❌ Requer mais setup técnico
- ❌ Precisa de GPU para performance boa

#### Opção 4: ML Tradicional (scikit-learn)
```python
# Clustering de padrões emocionais
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler

# Features dos últimos 30 dias
features = extrair_features(historico)  # [dias_positivos, variacao, etc]

# Agrupar usuários com padrões similares
clusters = KMeans(n_clusters=5).fit(features)

# Recomendações baseadas no cluster
recomendacoes = get_recomendacoes_do_cluster(usuario.cluster_id)
```

### 3.3 Arquitetura Sugerida com IA

```
┌─────────────────┐
│   Frontend      │
│  (React/Vue)    │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────┐
│   API Gateway / Backend         │
│   (Node.js/Python/Java)         │
├─────────────────────────────────┤
│  Endpoints:                     │
│  - POST /historico              │
│  - GET  /insights               │
│  - GET  /analise-ia             │
└────┬─────────────┬──────────────┘
     │             │
     ▼             ▼
┌─────────┐   ┌──────────────────┐
│ MySQL/  │   │   Serviço IA     │
│ Postgres│   │  (microservice)  │
└─────────┘   ├──────────────────┤
              │ - OpenAI API     │
              │ - Claude API     │
              │ - Local ML       │
              │ - Cache Redis    │
              └──────────────────┘
```

**Padrão recomendado:**
1. Backend persiste dados (MySQL/Postgres)
2. Job assíncrono (cron/queue) processa análises diárias
3. Serviço IA separado para não bloquear requests
4. Cache para evitar custos repetidos (Redis)

### 3.4 ⚠️ CONSIDERAÇÕES ÉTICAS CRÍTICAS

#### DEVE FAZER: ✅

1. **Transparência Total**
```javascript
// No primeiro uso, mostrar:
const termoConsentimento = `
Análise por IA:
- Usamos inteligência artificial para fornecer insights personalizados
- A IA NÃO é um profissional de saúde mental
- Seus dados são analisados de forma privada e segura
- Você pode desativar a análise IA a qualquer momento

Aceita continuar?
[Sim] [Não] [Saiba mais]
`;
```

2. **Disclaimers Visíveis**
```javascript
// Em TODA resposta de IA:
{
  "sugestao_ia": "...",
  "disclaimer": "Esta análise é feita por IA e não substitui profissionais de saúde mental.",
  "recursos_emergencia": {
    "cvv": "188",
    "caps": "..."
  }
}
```

3. **Opt-out Fácil**
```sql
-- Adicionar campo na tabela usuarios
ALTER TABLE usuarios ADD COLUMN usar_analise_ia BOOLEAN DEFAULT TRUE;

-- Permitir desativar sem perder funcionalidades básicas
```

4. **Criptografia e LGPD**
```javascript
// Dados sensíveis criptografados
const dadosParaIA = {
  usuario_id: hash(usuario.id),  // Anonimizar
  idade: usuario.idade,
  genero: usuario.genero,
  historico: usuario.historico.map(h => ({
    data: h.data,
    estado: h.estado,  // Enum, não texto livre
    // NÃO enviar: nome, email, telefone
  }))
};
```

5. **Contatos de Emergência Sempre Visíveis**
```javascript
// Em TODAS as telas do app
const recursos = {
  emergencia: {
    cvv: { telefone: '188', descricao: 'Centro de Valorização da Vida (24h, gratuito)' },
    samu: { telefone: '192', descricao: 'Emergências médicas' },
    caps: { link: 'https://caps.saude.gov.br', descricao: 'Centro de Atenção Psicossocial' }
  }
};
```

6. **Não Treinar com Dados de Usuários**
```python
# ❌ NUNCA fazer:
# fine_tune_model(user_data)

# ✅ Usar apenas modelos pré-treinados
# ✅ Se treinar, usar datasets públicos anonimizados
```

#### NÃO DEVE FAZER: ❌

1. ❌ **Diagnosticar Condições**
```javascript
// ❌ MAL:
"Você apresenta sinais de depressão clínica"

// ✅ BOM:
"Notamos que você tem se sentido para baixo nos últimos dias. Considere conversar com um profissional."
```

2. ❌ **Prescrever Tratamentos**
```javascript
// ❌ MAL:
"Recomendamos tomar sertralina 50mg"
"Você precisa de terapia cognitivo-comportamental"

// ✅ BOM:
"Exercícios físicos podem ajudar no bem-estar. Que tal tentar?"
"Profissionais de saúde mental podem oferecer suporte especializado"
```

3. ❌ **Substituir Profissionais**
```javascript
// ❌ MAL:
"Não precisa de psicólogo, use nosso app"

// ✅ BOM:
"Este app complementa, mas não substitui, acompanhamento profissional"
```

4. ❌ **Garantir Resultados**
```javascript
// ❌ MAL:
"Seguindo nossas sugestões, você vai se curar"

// ✅ BOM:
"Essas sugestões podem ajudar, mas cada pessoa é única"
```

5. ❌ **Compartilhar Dados Sem Consentimento**
```python
# ❌ NUNCA:
# - Vender dados de usuários
# - Compartilhar com seguradoras/empregadores
# - Usar para publicidade direcionada
# - Enviar para análise de terceiros sem opt-in explícito
```

6. ❌ **Armazenar Dados de IA de Forma Insegura**
```python
# ❌ MAL:
# prompt = f"Usuário {nome} com CPF {cpf} disse: {texto_livre}"

# ✅ BOM:
# - Anonimizar identificadores
# - Usar enums ao invés de texto livre
# - Criptografar em repouso
# - Logs sem PII (Personally Identifiable Information)
```

### 3.5 Exemplo de Implementação Ética Completa

```javascript
// ===== API Endpoint com IA =====
app.get('/usuarios/:id/analise-ia', authMiddleware, async (req, res) => {
  const usuario = await Usuario.findById(req.params.id);

  // 1. Verificar consentimento
  if (!usuario.consentimento_analise_ia) {
    return res.status(403).json({
      erro: 'Análise IA não autorizada',
      mensagem: 'Você precisa ativar a análise por IA nas configurações',
      recursos_sem_ia: ['/historico', '/estatisticas']
    });
  }

  // 2. Buscar histórico
  const historico = await HistoricoEmocional.findByUserId(usuario.id, { dias: 30 });

  // 3. Análise local (ML tradicional)
  const features = extrairFeatures(historico);
  const tendencia = analisarTendencia(features);
  const alertas = verificarSinaisAlerta(features);

  // 4. Gerar insights com LLM (se habilitado)
  let insightsIA = null;
  if (usuario.usar_llm) {
    // Anonimizar dados
    const dadosAnonimos = {
      idade: calcularIdade(usuario.dataNascimento),
      genero: usuario.genero,
      historico_estados: historico.map(h => h.estadoEmocional), // Apenas enum
      historico_atividades: historico.map(h => h.atividadeRealizada)
      // NÃO enviar: nome, email, telefone, id
    };

    insightsIA = await gerarInsightsComIA(dadosAnonimos);
  }

  // 5. Resposta com disclaimers
  res.json({
    analise: {
      periodo: '30 dias',
      tendencia: tendencia,
      correlacoes: features.correlacoes,
      insights_ia: insightsIA
    },
    alertas: alertas,

    // Disclaimers OBRIGATÓRIOS
    disclaimer: {
      texto: 'Esta análise é feita por inteligência artificial e não substitui profissionais de saúde mental.',
      data_analise: new Date(),
      versao_modelo: 'gpt-4-turbo-2024-04-09'
    },

    // Recursos SEMPRE presentes
    recursos_profissionais: {
      emergencia: {
        cvv: { telefone: '188', disponibilidade: '24h gratuito' },
        caps: { site: 'https://caps.saude.gov.br' }
      },
      terapia: [
        'Consulte o SUS para atendimento gratuito',
        'Planos de saúde cobrem psicologia/psiquiatria'
      ]
    },

    // Controle do usuário
    configuracoes: {
      desativar_ia: '/usuarios/configuracoes/ia',
      exportar_dados: '/usuarios/exportar-dados',
      deletar_conta: '/usuarios/deletar'
    }
  });
});
```

---

## 4. ROADMAP DE REFATORAÇÃO

### Fase 1: Correções Críticas (1-2 dias)
- [ ] Corrigir bug do DELETE mapping
- [ ] Remover campos redundantes de Usuario (estadoEmocional, atividadeRealizada)
- [ ] Criar migration para dropar colunas
- [ ] Adicionar validações básicas (@NotNull, @Email, @Past)
- [ ] Implementar @ControllerAdvice para tratamento de erros

### Fase 2: Segurança (2-3 dias)
- [ ] Implementar JWT token generation
- [ ] Criar JwtTokenService
- [ ] Criar SecurityFilter
- [ ] Implementar UserDetailsService
- [ ] Testar autenticação end-to-end
- [ ] Remover credenciais hardcoded (usar variáveis de ambiente)

### Fase 3: Refatoração REST (2 dias)
- [ ] Refatorar controllers para retornar ResponseEntity
- [ ] Padronizar códigos HTTP
- [ ] POST deve retornar recurso criado
- [ ] Adicionar HATEOAS básico (links)

### Fase 4: Camada de Serviço (1-2 dias)
- [ ] Extrair lógica de negócio dos controllers
- [ ] Criar UsuarioService
- [ ] Criar HistoricoEmocionalService
- [ ] Criar AutenticacaoService

### Fase 5: Testes (3-4 dias)
- [ ] Testes unitários para services
- [ ] Testes de integração para repositories
- [ ] Testes de API (REST Assured ou MockMvc)
- [ ] Coverage mínimo de 70%

### Fase 6: Documentação (1 dia)
- [ ] Adicionar Swagger/OpenAPI
- [ ] Documentar todos os endpoints
- [ ] Criar collection do Postman
- [ ] README com instruções de setup

### Fase 7: IA - MVP (3-5 dias)
- [ ] Adicionar consentimento de análise IA
- [ ] Implementar análise de padrões básica (ML tradicional)
- [ ] Integrar OpenAI/Claude para mensagens
- [ ] Endpoint de insights semanais
- [ ] Adicionar disclaimers e recursos de emergência
- [ ] Sistema de detecção de sinais de alerta (conservador)

### Fase 8: Migração de Tecnologia (SE decidir migrar)
**Opção Python:**
- [ ] Setup FastAPI + SQLAlchemy
- [ ] Migrar modelos (Usuario, Historico, Credenciais)
- [ ] Migrar endpoints um por um
- [ ] Migrar autenticação
- [ ] Testes de regressão
- [ ] Deploy

**Opção Node.js:**
- [ ] Setup Express + Prisma
- [ ] Migrar schema (schema.prisma)
- [ ] Migrar rotas
- [ ] Migrar autenticação (Passport.js)
- [ ] Testes de regressão
- [ ] Deploy

---

## 5. DECISÕES A TOMAR

### 5.1 Linguagem/Framework
**Decisão:** [ ] Manter Java | [ ] Migrar Python | [ ] Migrar Node.js

**Critérios:**
- **Manter Java:** Se objetivo é aprender Spring Boot especificamente
- **Python:** Se foco é IA/ML e análise de dados
- **Node.js:** Se foco é desenvolvimento rápido e frontend integration

### 5.2 Estratégia de IA
**Decisão:** [ ] OpenAI | [ ] Anthropic Claude | [ ] Hugging Face | [ ] Híbrido

**Critérios:**
- **OpenAI:** Boa qualidade, custo médio, fácil integração
- **Claude:** Melhor para saúde mental, custo similar
- **Hugging Face:** Gratuito, privacidade total, mais complexo
- **Híbrido:** ML local + LLM para mensagens (recomendado)

### 5.3 Banco de Dados
**Decisão:** [ ] Manter MySQL | [ ] Migrar PostgreSQL

**Recomendação:** PostgreSQL
- Melhor suporte a JSON (útil para features de IA)
- Full-text search nativo
- Mais features (window functions, etc)
- Preferido em projetos modernos

### 5.4 Deploy
**Decisão:** [ ] VPS tradicional | [ ] Docker | [ ] Serverless | [ ] PaaS

**Opções:**
- **VPS:** DigitalOcean, Linode (~$5-10/mês)
- **Docker:** AWS ECS, Google Cloud Run
- **Serverless:** Vercel (Node), AWS Lambda
- **PaaS:** Heroku, Railway, Render (mais fácil)

---

## 6. RECURSOS DE APRENDIZADO

### Java/Spring Boot
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)

### Python/FastAPI
- [FastAPI Documentation](https://fastapi.tiangolo.com/)
- [SQLAlchemy Tutorial](https://docs.sqlalchemy.org/en/20/tutorial/)

### Node.js/Express
- [Express.js Guide](https://expressjs.com/en/guide/routing.html)
- [Prisma Documentation](https://www.prisma.io/docs)

### IA/ML
- [OpenAI API Documentation](https://platform.openai.com/docs)
- [Anthropic Claude API](https://docs.anthropic.com/)
- [Hugging Face Transformers](https://huggingface.co/docs/transformers)
- [LangChain](https://python.langchain.com/docs/get_started/introduction)

### Ética em IA para Saúde
- [WHO Guidelines on AI for Health](https://www.who.int/publications/i/item/9789240029200)
- [ACM Code of Ethics](https://www.acm.org/code-of-ethics)

---

## 7. PRÓXIMOS PASSOS

**Imediato:**
1. Decidir se mantemos Java ou migramos (definir em reunião)
2. Corrigir bugs críticos identificados
3. Implementar autenticação JWT completa

**Curto prazo:**
4. Adicionar testes básicos
5. Refatorar para padrões REST adequados
6. Documentar API com Swagger

**Médio prazo:**
7. MVP de análise IA (ML tradicional primeiro)
8. Integração com LLM para mensagens
9. Sistema de insights semanais

**Longo prazo:**
10. Frontend completo (React/Vue)
11. Deploy em produção
12. Monitoramento e analytics
13. Expansão de features de IA

---

**Última atualização:** 2025-10-29
**Status:** Aguardando decisões sobre tecnologia e início da refatoração
