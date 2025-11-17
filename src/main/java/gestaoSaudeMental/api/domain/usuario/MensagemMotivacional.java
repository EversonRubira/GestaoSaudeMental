package gestaoSaudeMental.api.domain.usuario;

import gestaoSaudeMental.api.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class MensagemMotivacional {

    private static AIService aiService;

    private static final Map<EstadoEmocionalEnum, Map<AtividadeRealizadaEnum, String>> mensagensFallback = new EnumMap<>(EstadoEmocionalEnum.class);

    static {
        // Mensagens de fallback caso a IA não esteja disponível
        Map<AtividadeRealizadaEnum, String> mensagensFeliz = new EnumMap<>(AtividadeRealizadaEnum.class);
        mensagensFeliz.put(AtividadeRealizadaEnum.EXERCICIO, "%s, mantenha essa energia positiva enquanto se exercita!");
        mensagensFeliz.put(AtividadeRealizadaEnum.LEITURA, "%s, aproveite sua felicidade com um bom livro.");
        mensagensFallback.put(EstadoEmocionalEnum.FELIZ, mensagensFeliz);

        Map<AtividadeRealizadaEnum, String> mensagensTriste = new EnumMap<>(AtividadeRealizadaEnum.class);
        mensagensTriste.put(AtividadeRealizadaEnum.MEDITAR, "%s, meditar pode ajudar a trazer mais calma em momentos difíceis.");
        mensagensTriste.put(AtividadeRealizadaEnum.ASSISTIR_TV, "%s, permita-se relaxar enquanto assiste algo que você gosta.");
        mensagensTriste.put(AtividadeRealizadaEnum.TRABALHO, "%s, mesmo nos dias difíceis, lembre-se de cuidar de si. Você é importante.");
        mensagensFallback.put(EstadoEmocionalEnum.TRISTE, mensagensTriste);

        mensagensFallback.put(EstadoEmocionalEnum.ANSIOSO, Map.of(
                AtividadeRealizadaEnum.EXERCICIO, "%s, exercícios podem ajudar a aliviar a ansiedade.",
                AtividadeRealizadaEnum.MEDITAR, "%s, a meditação é uma ótima maneira de acalmar a mente."
        ));

        mensagensFallback.put(EstadoEmocionalEnum.CANSADO, Map.of());
        mensagensFallback.put(EstadoEmocionalEnum.IRRITADO, Map.of());
    }

    @Autowired
    public void setAIService(AIService aiService) {
        MensagemMotivacional.aiService = aiService;
    }

    public static String obterMensagem(EstadoEmocionalEnum estado, AtividadeRealizadaEnum atividade, String nome) {
        // Tenta usar IA primeiro
        if (aiService != null) {
            try {
                String mensagemIA = aiService.gerarMensagemMotivacional(
                    nome,
                    estado.name(),
                    atividade.name()
                );
                if (mensagemIA != null && !mensagemIA.isEmpty()) {
                    return mensagemIA;
                }
            } catch (Exception e) {
                System.err.println("Erro ao gerar mensagem com IA, usando fallback: " + e.getMessage());
            }
        }

        // Fallback para mensagens pré-configuradas
        Map<AtividadeRealizadaEnum, String> mensagensPorAtividade = mensagensFallback.getOrDefault(estado, Map.of());
        String mensagemFormatada = mensagensPorAtividade.getOrDefault(atividade, "%s, Você está indo bem! Continue cuidando de si.");
        return String.format(mensagemFormatada, nome);
    }
}

