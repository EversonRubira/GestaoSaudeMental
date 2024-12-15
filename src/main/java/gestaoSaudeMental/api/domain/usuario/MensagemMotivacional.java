package gestaoSaudeMental.api.domain.usuario;

import java.util.EnumMap;
import java.util.Map;

public class MensagemMotivacional {

    private static final Map<EstadoEmocionalEnum, Map<AtividadeRealizadaEnum, String>> mensagens = new EnumMap<>(EstadoEmocionalEnum.class);

    static {
        // Mensagens para estado FELIZ
        Map<AtividadeRealizadaEnum, String> mensagensFeliz = new EnumMap<>(AtividadeRealizadaEnum.class);
        mensagensFeliz.put(AtividadeRealizadaEnum.EXERCICIO, "%s, mantenha essa energia positiva enquanto se exercita!");
        mensagensFeliz.put(AtividadeRealizadaEnum.LEITURA, "%s, aproveite sua felicidade com um bom livro.");
        mensagens.put(EstadoEmocionalEnum.FELIZ, mensagensFeliz);

        // Mensagens para estado TRISTE
        Map<AtividadeRealizadaEnum, String> mensagensTriste = new EnumMap<>(AtividadeRealizadaEnum.class);
        mensagensTriste.put(AtividadeRealizadaEnum.MEDITAR, "%s, meditar pode ajudar a trazer mais calma em momentos difíceis.");
        mensagensTriste.put(AtividadeRealizadaEnum.ASSISTIR_TV, "%s, permita-se relaxar enquanto assiste algo que você gosta.");
        mensagensTriste.put(AtividadeRealizadaEnum.TRABALHO, "%s, mesmo nos dias difíceis, lembre-se de cuidar de si. Você é importante.");
        mensagens.put(EstadoEmocionalEnum.TRISTE, mensagensTriste);

        // Mensagens genéricas para outros estados emocionais
        mensagens.put(EstadoEmocionalEnum.ANSIOSO, Map.of(
                AtividadeRealizadaEnum.EXERCICIO, "%s, exercícios podem ajudar a aliviar a ansiedade.",
                AtividadeRealizadaEnum.MEDITAR, "%s, a meditação é uma ótima maneira de acalmar a mente."
        ));

        // Mensagens padrão para estados emocionais sem combinações específicas
        mensagens.put(EstadoEmocionalEnum.CANSADO, Map.of());
        mensagens.put(EstadoEmocionalEnum.IRRITADO, Map.of());
    }

    public static String obterMensagem(EstadoEmocionalEnum estado, AtividadeRealizadaEnum atividade, String nome) {
        Map<AtividadeRealizadaEnum, String> mensagensPorAtividade = mensagens.getOrDefault(estado, Map.of());

        String mensagemFormatada = mensagensPorAtividade.getOrDefault(atividade, "%s, Você está indo bem! Continue cuidando de si.");

            return String.format(mensagemFormatada, nome);
        }

}

