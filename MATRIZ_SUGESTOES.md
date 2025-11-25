# 🧠 Matriz de Sugestões Inteligentes - Gestão Saúde Mental

## Lógica de Recomendação: Emoção × Energia × Contexto

Esta matriz define as sugestões personalizadas baseadas em três variáveis:
1. **Emoção** (O que o usuário sente)
2. **Energia** (Capacidade física/mental disponível)
3. **Contexto** (O que pesou mais no dia)

---

## 📊 Estrutura de Dados

### Input do Usuário
```json
{
  "emocao": "TRISTE",
  "nivelEnergia": "BAIXO",
  "contextos": ["TRABALHO", "SONO"]
}
```

### Output do Sistema
```json
{
  "categoria": "Descompressão Suave",
  "sugestaoPrincipal": {
    "titulo": "Banho Quente",
    "descricao": "15 minutos de água quente relaxam músculos e mente",
    "duracao": "15 min",
    "icone": "🛁",
    "guia": "url-do-guia"
  },
  "alternativas": [
    "Chá de camomila + Música instrumental",
    "Deitar sem tela por 20 minutos"
  ],
  "razao": "Você está mentalmente esgotado. Priorize descanso passivo."
}
```

---

## 🎭 Matriz Completa de Sugestões

### 1. TRISTEZA

#### Tristeza + Energia ESGOTADA/BAIXA
| Contexto | Sugestão Principal | Categoria | Duração | Alternativas |
|----------|-------------------|-----------|---------|--------------|
| **Trabalho** | Banho quente + Playlist calma | Descompressão Suave | 20 min | Chá, Journaling guiado, Descanso sem tela |
| **Relacionamento** | Assistir série leve (comédia) | Distração Acolhedora | 30 min | Mensagem para amigo, Acariciar pet, Foto-memória feliz |
| **Sono** | Meditação guiada para dormir | Recuperação | 10 min | Leite morno, Leitura leve, Ambient sounds |
| **Saúde** | Ligação para pessoa de confiança | Conexão | 15 min | Escrever sentimentos, Áudio motivacional |
| **Notícias** | Detox digital + Música | Desconexão | 30 min | Podcast leve, Desenhar/Colorir |

**Lógica:** Tristeza com energia baixa = NÃO forçar movimento. Acolhimento passivo.

#### Tristeza + Energia MODERADA
| Contexto | Sugestão Principal | Categoria | Duração | Alternativas |
|----------|-------------------|-----------|---------|--------------|
| **Trabalho** | Caminhada leve (10 min) | Movimento Suave | 10 min | Alongamento, Respiração 4-7-8 |
| **Relacionamento** | Escrever carta (não enviar) | Processamento | 20 min | Ligação para amigo, Gratitude journal |
| **Sono** | Yoga restaurativa | Movimento Calmo | 15 min | Massagem auto-facial, Chá + leitura |
| **Saúde** | Cozinhar algo nutritivo | Auto-cuidado | 30 min | Vitamina de frutas, Ordenar quarto |
| **Notícias** | Voluntariado virtual rápido | Propósito | 20 min | Mensagem positiva, Ler história inspiradora |

**Lógica:** Alguma energia disponível = Movimento leve ajuda a processar emoção.

#### Tristeza + Energia BOA/PLENA
| Contexto | Sugestão Principal | Categoria | Duração | Alternativas |
|----------|-------------------|-----------|---------|--------------|
| **Trabalho** | Corrida ou Boxe | Descarga | 30 min | Dança intensa, Limpeza enérgica |
| **Relacionamento** | Conversa honesta (presencial) | Conexão Ativa | 40 min | Escrever + enviar, Terapia express |
| **Sono** | Reorganizar ambiente | Controle | 30 min | Exercício matinal, Playlist energética |
| **Saúde** | Consulta médica/terapia | Ação Prática | Variável | Lista de melhorias, Pesquisa de profissionais |
| **Notícias** | Ação social concreta | Impacto | 1h | Doação, Post consciente, Projeto pessoal |

**Lógica:** Energia alta permite ação transformadora. Redirecionar tristeza em movimento.

---

### 2. ANSIEDADE

#### Ansiedade + Energia ESGOTADA/BAIXA
| Contexto | Sugestão Principal | Categoria | Duração | Alternativas |
|----------|-------------------|-----------|---------|--------------|
| **Trabalho** | Respiração 4-7-8 (3 ciclos) | Regulação | 5 min | Progressive muscle relaxation, ASMR |
| **Relacionamento** | Grounding 5-4-3-2-1 | Ancoragem | 5 min | Segurar objeto frio, Nomear emoções |
| **Sono** | Body scan guiado | Desaceleração | 15 min | Contagem regressiva, Visualização guiada |
| **Saúde** | Vídeo de EFT (Tapping) | Auto-regulação | 10 min | Mantra repetitivo, Hand warming |
| **Notícias** | Limite de 5 min de tela + natureza | Desconexão | 15 min | Earthing (pés no chão), Água gelada no rosto |

**Lógica:** Ansiedade + Esgotamento = Sistema nervoso sobrecarregado. Foco em regulação rápida.

#### Ansiedade + Energia MODERADA
| Contexto | Sugestão Principal | Categoria | Duração | Alternativas |
|----------|-------------------|-----------|---------|--------------|
| **Trabalho** | Lista "Urgente vs Importante" | Organização | 10 min | Pomodoro 25 min, Time blocking |
| **Relacionamento** | Mensagem estruturada (não impulsiva) | Comunicação Consciente | 15 min | Espera 2h, Escrever rascunho |
| **Sono** | Yoga Nidra | Reset Neural | 20 min | Chá + rotina fixa, Desligar Wi-Fi |
| **Saúde** | Caminhada sem fone | Presença | 15 min | Jardinagem, Arrumar gaveta |
| **Notícias** | Fact-check + limitar fontes | Controle | 10 min | Escolher 1 fonte confiável, Mute notificações |

**Lógica:** Ansiedade com energia moderada = Canalizar em organização e estrutura.

#### Ansiedade + Energia BOA/PLENA
| Contexto | Sugestão Principal | Categoria | Duração | Alternativas |
|----------|-------------------|-----------|---------|--------------|
| **Trabalho** | HIIT ou Corrida | Descarga Física | 20 min | Pular corda, Boxe, Natação |
| **Relacionamento** | Conversa + caminhada | Movimento + Conexão | 30 min | Esporte em dupla, Cozinhar juntos |
| **Sono** | Exercício matinal intenso | Gasto Saudável | 30 min | Spinning, CrossFit, Escalada |
| **Saúde** | Consulta + plano de ação | Empowerment | Variável | Exames, Novo hábito (rastreado) |
| **Notícias** | Ativismo estruturado | Propósito | 1h | Petição, Voluntariado, Educação |

**Lógica:** Energia alta com ansiedade = Descarregar adrenalina em movimento intenso.

---

### 3. NEUTRO/PLENO

#### Neutro + Energia BAIXA
| Contexto | Sugestão Principal | Categoria | Duração | Alternativas |
|----------|-------------------|-----------|---------|--------------|
| **Qualquer** | Manutenção de rotina | Estabilidade | Variável | Sono de qualidade, Hidratação, Refeição leve |

#### Neutro + Energia MODERADA
| Contexto | Sugestão Principal | Categoria | Duração | Alternativas |
|----------|-------------------|-----------|---------|--------------|
| **Qualquer** | Atividade criativa | Exploração | 30 min | Aprender algo novo, Projeto pessoal, Hobby |

#### Neutro + Energia ALTA
| Contexto | Sugestão Principal | Categoria | Duração | Alternativas |
|----------|-------------------|-----------|---------|--------------|
| **Qualquer** | Atividade social + movimento | Conexão | 1h | Esporte em grupo, Evento, Ajudar alguém |

---

### 4. FELIZ/RADIANTE

#### Feliz + Qualquer Energia
| Contexto | Sugestão Principal | Categoria | Duração | Alternativas |
|----------|-------------------|-----------|---------|--------------|
| **Qualquer** | Registrar o momento | Memória | 5 min | Foto, Journaling positivo, Mensagem para amigo |
| **Trabalho** | Capitalizar produtividade | Momentum | Variável | Tarefa difícil, Projeto criativo |
| **Relacionamento** | Fortalecer conexão | Reciprocidade | 30 min | Presente surpresa, Elogio genuíno |

**Lógica:** Felicidade = Ancorar memória + usar energia positiva estrategicamente.

---

## 🔄 Sistema de Aprendizado

### Feedback Loop

```
Usuário recusa sugestão
    ↓
Registrar: [Emoção + Energia + Contexto + Sugestão Recusada]
    ↓
Oferecer Alternativa da lista
    ↓
Se aceita: Aumentar peso dessa sugestão no perfil do usuário
Se recusa novamente: Marcar combinação como "não funciona para este usuário"
```

### Personalização Progressiva

Após 30 dias de uso:
```
Usuário "João"
- Nunca aceita "Corrida" quando contexto = "Trabalho"
- Sempre aceita "Chá" quando Energia = "Baixa"
- Prefere "Podcast" a "Música"

→ Ajustar algoritmo para este perfil específico
```

---

## 🎨 UI/UX Detalhado

### Tela de Check-in

```
┌─────────────────────────────────────┐
│  Boa noite, João! 🌙                │
│                                     │
│  Como você está se sentindo?        │
│  ┌─────────────────────────────┐   │
│  │ 😫  😢  😐  🙂  🤩          │   │
│  └─────────────────────────────┘   │
│                                     │
│  Qual seu nível de energia?         │
│  ┌─────────────────────────────┐   │
│  │ ●─────────○──────────────── │   │
│  │ Bateria Baixa    Bateria Full│   │
│  └─────────────────────────────┘   │
│                                     │
│  O que pesou mais hoje?             │
│  ┌──────┐ ┌──────┐ ┌──────┐       │
│  │Trabalho│ │Sono │ │Saúde │       │
│  └──────┘ └──────┘ └──────┘       │
│  ┌──────┐ ┌──────┐                │
│  │Relac. │ │Notícias│               │
│  └──────┘ └──────┘                │
│                                     │
│         [Gerar Sugestão]            │
└─────────────────────────────────────┘
```

### Tela de Sugestão

```
┌─────────────────────────────────────┐
│  ← Voltar                          │
│                                     │
│  🛁 Descompressão Suave             │
│                                     │
│  Banho Quente (15 min)              │
│  ─────────────────────────────────  │
│  Água quente relaxa músculos        │
│  tensionados pelo trabalho e        │
│  prepara o corpo para descanso.     │
│                                     │
│  [🎵 Tocar Playlist Relaxante]     │
│  [📖 Ver Guia Passo a Passo]       │
│                                     │
│  Por que isso?                      │
│  Você está mentalmente esgotado.    │
│  Priorize descanso passivo agora.   │
│                                     │
│  ─────────────────────────────────  │
│  Outras opções:                     │
│  • Chá de camomila                  │
│  • Deitar sem tela                  │
│                                     │
│  [✅ Vou Fazer Isso]               │
│  [↻ Sugerir Outra Coisa]           │
└─────────────────────────────────────┘
```

---

## 💾 Implementação Backend

### Nova Entidade
```java
@Entity
public class HistoricoEmocional {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private EstadoEmocionalEnum estadoEmocional;

    @Enumerated(EnumType.STRING)
    private NivelEnergiaEnum nivelEnergia;  // NOVO!

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ContextoEnum> contextos;  // Múltiplos contextos

    private LocalDateTime dataRegistro;

    @ManyToOne
    private Sugestao sugestaoRecebida;

    @Enumerated(EnumType.STRING)
    private FeedbackSugestaoEnum feedback;  // ACEITA, RECUSADA, ALTERNATIVA
}
```

### Algoritmo de Sugestão
```java
@Service
public class SugestaoService {

    public SugestaoDTO gerarSugestao(
        EstadoEmocionalEnum emocao,
        NivelEnergiaEnum energia,
        List<ContextoEnum> contextos,
        Long usuarioId
    ) {
        // 1. Buscar matriz de sugestões
        List<Sugestao> candidatas = sugestaoRepository
            .findByEmocaoAndEnergia(emocao, energia);

        // 2. Filtrar por contexto
        candidatas = filtrarPorContexto(candidatas, contextos);

        // 3. Aplicar histórico do usuário (machine learning simples)
        candidatas = ordenarPorPreferenciasUsuario(candidatas, usuarioId);

        // 4. Retornar top 1 + 2 alternativas
        return new SugestaoDTO(
            candidatas.get(0),  // Principal
            candidatas.subList(1, 3)  // Alternativas
        );
    }

    private List<Sugestao> ordenarPorPreferenciasUsuario(
        List<Sugestao> sugestoes,
        Long usuarioId
    ) {
        Map<Long, Double> scores = new HashMap<>();

        for (Sugestao s : sugestoes) {
            // Buscar histórico de feedback do usuário
            long aceitas = feedbackRepository.countAceitas(usuarioId, s.getId());
            long recusadas = feedbackRepository.countRecusadas(usuarioId, s.getId());

            double score = (aceitas * 1.0) - (recusadas * 0.5);
            scores.put(s.getId(), score);
        }

        // Ordenar por score decrescente
        sugestoes.sort((s1, s2) ->
            Double.compare(scores.get(s2.getId()), scores.get(s1.getId()))
        );

        return sugestoes;
    }
}
```

---

## 📊 Métricas de Sucesso

Para validar se o sistema funciona:

1. **Taxa de Aceitação**: % de sugestões aceitas (meta: >60%)
2. **Tempo de Resposta**: Usuário aceita em <30s? (meta: 70%)
3. **Taxa de Retorno**: Usuário volta no dia seguinte? (meta: >50%)
4. **Melhoria Percebida**: Pergunta semanal "Você se sente melhor?" (meta: >70% sim)

---

## 🚀 Roadmap de Implementação

### Fase 1 (MVP)
- ✅ Adicionar campo `nivelEnergia`
- ✅ Implementar matriz fixa de sugestões
- ✅ UI com emoji + slider + tags
- ✅ Endpoint `/sugestao` que retorna JSON

### Fase 2 (Inteligência)
- 📊 Registrar feedback (aceita/recusa)
- 🧠 Algoritmo básico de preferências
- 📈 Dashboard de histórico (gráfico emoção x energia)

### Fase 3 (Avançado)
- 🤖 Machine Learning (TensorFlow Lite)
- 🔔 Notificações inteligentes (horário ótimo)
- 👥 Modo comunidade (sugestões de outros usuários)
- 🎯 Gamificação (streak, badges)

---

## ✨ Conclusão

Esta matriz transforma o app de um **diário passivo** em um **coach ativo** de saúde mental.

**Diferencial competitivo:**
- Daylio: Não tem sugestões personalizadas
- Moodpath: Focado em diagnóstico, não ação imediata
- **Seu App**: Triage + Ação personalizada em tempo real

**Próximo passo:** Quer que eu implemente o backend dessa matriz ou prefere focar no Android primeiro?
