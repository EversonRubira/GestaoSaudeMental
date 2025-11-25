# 📝 Notas Importantes - Para Próxima Sessão

## ⚠️ Correção de Mal-Entendido (2025-01-25)

### **O que FOI implementado (errado):**
Eu implementei uma estrutura completa de menopausa com:
- ❌ MenopausaEnum (4 estados)
- ❌ SintomasMenopausaEnum (12 sintomas)
- ❌ Lógica complexa de sugestões para menopausa
- ❌ Migration V11
- ❌ Atualização de DTOs e Controller

**ISSO FOI REVERTIDO!** (git reset --hard)

---

### **O que o usuário REALMENTE quer:**

> **APENAS UMA SOLUÇÃO SIMPLES DE UI:**
> - Um **botão/toggle** na interface do app Android
> - Pergunta: "Você ainda menstrua?" ou "Não menstruo mais"
> - Se a mulher clicar que **NÃO menstrua mais**:
>   - As perguntas de ciclo menstrual **desaparecem da tela**
>   - Ela usa o app normalmente (emoção + energia + contexto)
> - Se ela clicar que **SIM menstrua**:
>   - Mostra perguntas de ciclo menstrual normalmente

**Solução técnica:**
- Adicionar campo `menstruaAtualmente: Boolean` no cadastro de Usuario (apenas para FEMININO)
- No frontend Android: `if (genero == FEMININO && menstruaAtualmente == true) { mostrar perguntas ciclo }`
- **NÃO PRECISA** de enums complexos, sintomas de menopausa, nem lógica específica

---

## ✅ Estado Atual do Backend (Confirmado)

### **Implementado e Funcionando:**
- [x] API REST completa
- [x] Autenticação JWT
- [x] Sistema de sugestões (Emoção × Energia × Contexto)
- [x] Ciclo menstrual + Sintomas físicos (para mulheres que menstruam)
- [x] Machine Learning simples (aprende preferências do usuário)
- [x] Migrations V1-V10

### **Enums Disponíveis:**
```java
EstadoEmocionalEnum: TRISTE, ANSIOSO, NEUTRO, FELIZ, MOTIVADO, ESGOTADO
NivelEnergiaEnum: ESGOTADO, BAIXO, MODERADO, BOM, PLENO
ContextoEnum: TRABALHO, RELACIONAMENTO, SONO, SAUDE, NOTICIAS, NADA_ESPECIFICO
CicloMenstrualEnum: NAO_APLICAVEL, SIM_MENSTRUADA, NAO_MENSTRUADA
SintomaFisicoEnum: COLICA, DOR_CABECA, ENJOO, FADIGA, INCHACO, DOR_SEIOS, NENHUM
FeedbackSugestaoEnum: ACEITA, RECUSADA, ALTERNATIVA
```

### **Entidades:**
- Usuario (id, nome, email, telefone, dataNascimento, genero, ativo)
- HistoricoEmocional (estadoEmocional, nivelEnergia, contextos, cicloMenstrual, sintomasFisicos, sugestaoRecebida, feedbackSugestao)
- Sugestao (titulo, descricao, categoria, duracao, icone, razao, alternativas)
- Credenciais (usuario, senhaHash)

### **Endpoints Principais:**
```
POST /usuarios - Cadastro
POST /usuarios/{id}/sugestao - Obter sugestão personalizada
POST /usuarios/{id}/historico-completo - Registrar check-in com feedback
GET /usuarios/{id}/historico_por_periodo - Listar histórico
GET /usuarios/{id}/historico-cronologico - Listar por emoção
```

---

## 🎯 Próximos Passos REAIS

### **1. Ajuste Simples no Backend (opcional):**
Se quiser persistir se a mulher menstrua ou não:
```java
// Em Usuario.java
private Boolean menstruaAtualmente; // null para homens, true/false para mulheres
```

Mas isso pode ser apenas **no frontend** (mais simples).

---

### **2. Implementar App Android:**

#### **Cadastro:**
```
Nome → Email → Senha → Data Nascimento → Gênero

Se FEMININO:
  → "Você menstrua atualmente?" [SIM] [NÃO]

Se MASCULINO:
  → (pula essa pergunta)
```

#### **Check-in Diário:**
```
TODOS:
1. Como está? (😫 😢 😐 🙂 🤩)
2. Energia? (slider 0-100%)
3. O que pesou? (Trabalho, Sono, Saúde...)

SE FEMININO + MENSTRUA:
4. Está no período? (Sim/Não)
5. Sintomas? (Cólica, Fadiga, Enjoo...)

SE FEMININO + NÃO MENSTRUA:
(pula perguntas 4 e 5)

SE MASCULINO:
(pula perguntas 4 e 5)
```

---

### **3. Implementar Sistema Premium (Backend):**
- [ ] Enum PlanoEnum (FREE, PREMIUM)
- [ ] Campo `plano` em Usuario
- [ ] Endpoint `/usuarios/{id}/assinar-premium`
- [ ] Validação de acesso em endpoints premium

---

### **4. Documentação:**
- ✅ DESIGN.md criado (paleta de cores, tipografia, componentes)
- ✅ ROADMAP.md criado (timeline, funcionalidades, métricas)
- ✅ ARQUITETURA.md existente
- ✅ COMPARACAO_ARQUITETURAS.md existente
- ✅ MATRIZ_SUGESTOES.md existente

---

## 💡 Lembretes do Usuário

1. **"O que pesou?" deve aparecer para TODOS** (homens e mulheres)
2. **Mulheres 50+ que não menstruam** → apenas marcar "Não menstruo mais" e usar app normal
3. **Menopausa NÃO precisa de sugestões específicas** → app funciona normal sem perguntas de ciclo
4. **Aguardando Kobo chegar** para criar livro didático de arquitetura
5. **Modelo de negócio:** Freemium €1,99/mês (Portugal), lançamento PT/BR/ES

---

## 🚀 Resumo do Que Falta

### **Backend:**
- [ ] Sistema de planos (Free/Premium)
- [ ] Endpoints de estatísticas/gráficos
- [ ] (Opcional) Campo `menstruaAtualmente` em Usuario

### **Android:**
- [ ] Criar projeto Android completo
- [ ] Implementar login/cadastro (com lógica de gênero)
- [ ] Check-in diário condicional
- [ ] Tela de sugestões
- [ ] Histórico e gráficos
- [ ] Multi-idioma (PT, EN, ES)
- [ ] Google Play Billing

---

**Última atualização:** 2025-01-25 (após correção de mal-entendido)
**Status:** Pronto para próxima sessão
