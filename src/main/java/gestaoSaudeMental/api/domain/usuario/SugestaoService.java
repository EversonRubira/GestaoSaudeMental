package gestaoSaudeMental.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SugestaoService {

    @Autowired
    private SugestaoRepository sugestaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Gera sugestão personalizada baseada em emoção, energia, contextos, ciclo menstrual e sintomas
     */
    public DadosSugestaoDTO gerarSugestao(
            EstadoEmocionalEnum emocao,
            NivelEnergiaEnum energia,
            List<ContextoEnum> contextos,
            CicloMenstrualEnum cicloMenstrual,
            List<SintomaFisicoEnum> sintomas,
            Long usuarioId
    ) {
        // PRIORIDADE 1: Se está menstruada COM sintomas intensos (cólica)
        if (cicloMenstrual == CicloMenstrualEnum.SIM_MENSTRUADA &&
            sintomas != null && sintomas.contains(SintomaFisicoEnum.COLICA)) {
            return getSugestaoMenstrualComColica(emocao, energia);
        }

        // PRIORIDADE 2: Se está menstruada SEM cólica (outros sintomas)
        if (cicloMenstrual == CicloMenstrualEnum.SIM_MENSTRUADA) {
            return getSugestaoMenstrualGeral(emocao, energia, sintomas);
        }

        // PRIORIDADE 3: Lógica normal (matriz padrão)
        // 1. Buscar candidatas por emoção e energia
        List<Sugestao> candidatas = sugestaoRepository
                .findByEstadoEmocionalAndNivelEnergiaAndAtivaTrue(emocao, energia);

        if (candidatas.isEmpty()) {
            // Fallback: buscar apenas por emoção
            candidatas = buscarPorEmocao(emocao);
        }

        // 2. Filtrar por contexto (se fornecido)
        if (contextos != null && !contextos.isEmpty()) {
            candidatas = filtrarPorContexto(candidatas, contextos);
        }

        // 3. Ordenar por preferências do usuário (machine learning simples)
        if (usuarioId != null) {
            candidatas = ordenarPorPreferenciasUsuario(candidatas, usuarioId);
        }

        // 4. Retornar a melhor sugestão
        if (!candidatas.isEmpty()) {
            return new DadosSugestaoDTO(candidatas.get(0));
        }

        // Fallback final: retornar sugestão genérica
        return getSugestaoGenerica(emocao);
    }

    /**
     * Filtra sugestões por contexto
     */
    private List<Sugestao> filtrarPorContexto(List<Sugestao> sugestoes, List<ContextoEnum> contextos) {
        return sugestoes.stream()
                .filter(s -> s.getContextos() != null &&
                        !Collections.disjoint(s.getContextos(), contextos))
                .collect(Collectors.toList());
    }

    /**
     * Ordena sugestões por preferências do usuário
     */
    private List<Sugestao> ordenarPorPreferenciasUsuario(List<Sugestao> sugestoes, Long usuarioId) {
        Map<Long, Double> scores = new HashMap<>();

        for (Sugestao s : sugestoes) {
            long aceitas = sugestaoRepository.countAceitas(usuarioId, s.getId());
            long recusadas = sugestaoRepository.countRecusadas(usuarioId, s.getId());

            // Score: +1 para cada aceita, -0.5 para cada recusada
            double score = (aceitas * 1.0) - (recusadas * 0.5);
            scores.put(s.getId(), score);
        }

        // Ordenar por score decrescente
        sugestoes.sort((s1, s2) ->
                Double.compare(
                        scores.getOrDefault(s2.getId(), 0.0),
                        scores.getOrDefault(s1.getId(), 0.0)
                )
        );

        return sugestoes;
    }

    /**
     * Busca sugestões apenas por emoção (fallback)
     */
    private List<Sugestao> buscarPorEmocao(EstadoEmocionalEnum emocao) {
        return sugestaoRepository.findAll().stream()
                .filter(s -> s.getEstadoEmocional() == emocao && s.getAtiva())
                .collect(Collectors.toList());
    }

    /**
     * Sugestão genérica quando não há match
     */
    private DadosSugestaoDTO getSugestaoGenerica(EstadoEmocionalEnum emocao) {
        String titulo, descricao, categoria, icone, razao;

        switch (emocao) {
            case TRISTE:
                titulo = "Tire um momento para você";
                descricao = "Respire fundo e lembre que tudo passa";
                categoria = "Acolhimento";
                icone = "😌";
                razao = "Momentos difíceis são temporários.";
                break;
            case ANSIOSO:
                titulo = "Respire conscientemente";
                descricao = "Pratique a respiração 4-7-8";
                categoria = "Regulação";
                icone = "🧘";
                razao = "A respiração acalma o sistema nervoso.";
                break;
            case FELIZ:
                titulo = "Registre este momento";
                descricao = "Anote o que te fez feliz hoje";
                categoria = "Gratidão";
                icone = "📝";
                razao = "Memórias positivas fortalecem a resiliência.";
                break;
            default:
                titulo = "Cuide de você";
                descricao = "Faça algo gentil por si mesmo";
                categoria = "Auto-cuidado";
                icone = "💚";
                razao = "Você merece se sentir bem.";
        }

        return new DadosSugestaoDTO(
                null,
                titulo,
                descricao,
                categoria,
                "10 min",
                icone,
                razao,
                List.of(),
                null,
                null
        );
    }

    /**
     * Sugestão específica para período menstrual COM cólica
     */
    private DadosSugestaoDTO getSugestaoMenstrualComColica(
            EstadoEmocionalEnum emocao,
            NivelEnergiaEnum energia
    ) {
        String titulo, descricao, razao;
        List<String> alternativas;

        // Independente da emoção, cólica é prioridade
        if (energia == NivelEnergiaEnum.ESGOTADO || energia == NivelEnergiaEnum.BAIXO) {
            titulo = "Bolsa Térmica + Repouso";
            descricao = "Deite-se em posição fetal com bolsa de água quente no abdômen por 20 minutos";
            razao = "Cólica menstrual + energia baixa exige repouso. O calor relaxa os músculos uterinos e alivia a dor.";
            alternativas = List.of(
                "Chá de gengibre morno",
                "Massagem abdominal suave em círculos",
                "Posição joelhos no peito (alivia pressão)"
            );
        } else {
            titulo = "Caminhada Suave + Calor";
            descricao = "Faça caminhada leve de 10 min, depois aplique calor no abdômen";
            razao = "Movimento suave aumenta circulação e pode reduzir cólica. Você tem energia para um leve exercício.";
            alternativas = List.of(
                "Yoga restaurativa (postura criança)",
                "Alongamento suave",
                "Natação leve (se disponível)"
            );
        }

        return new DadosSugestaoDTO(
                null,
                titulo,
                descricao,
                "Alívio Menstrual",
                "20 min",
                "🌸",
                razao,
                alternativas,
                null,
                null
        );
    }

    /**
     * Sugestão específica para período menstrual SEM cólica intensa
     */
    private DadosSugestaoDTO getSugestaoMenstrualGeral(
            EstadoEmocionalEnum emocao,
            NivelEnergiaEnum energia,
            List<SintomaFisicoEnum> sintomas
    ) {
        String titulo, descricao, razao;
        List<String> alternativas;

        boolean temFadiga = sintomas != null && sintomas.contains(SintomaFisicoEnum.FADIGA);
        boolean temEnjooDor = sintomas != null &&
                (sintomas.contains(SintomaFisicoEnum.ENJOO) ||
                 sintomas.contains(SintomaFisicoEnum.DOR_CABECA));

        if (temFadiga || energia == NivelEnergiaEnum.ESGOTADO || energia == NivelEnergiaEnum.BAIXO) {
            titulo = "Autocuidado Gentil";
            descricao = "Priorize descanso, hidratação e alimentos leves. Evite atividades intensas.";
            razao = "Durante o período menstrual com fadiga, o corpo precisa de energia para recuperação.";
            alternativas = List.of(
                "Cochilo de 20 minutos",
                "Chá relaxante + leitura leve",
                "Banho morno"
            );
        } else if (temEnjooDor) {
            titulo = "Hidratação + Ambiente Calmo";
            descricao = "Beba água, reduza luminosidade e evite telas por 30 minutos";
            razao = "Enjoo e dor de cabeça durante o período podem ser aliviados com descanso sensorial.";
            alternativas = List.of(
                "Compressa fria na testa",
                "Aromaterapia (lavanda)",
                "Deitar em ambiente escuro"
            );
        } else if (emocao == EstadoEmocionalEnum.TRISTE || emocao == EstadoEmocionalEnum.ANSIOSO) {
            titulo = "Movimento Suave + Conexão";
            descricao = "Yoga leve ou caminhada tranquila. Converse com alguém querido.";
            razao = "Alterações hormonais podem intensificar emoções. Movimento gentil e conexão ajudam.";
            alternativas = List.of(
                "Journaling (escrever sentimentos)",
                "Ouvir música reconfortante",
                "Assistir algo leve"
            );
        } else {
            // Feliz ou neutro com energia razoável
            titulo = "Aproveite com Moderação";
            descricao = "Você pode manter atividades normais, mas evite exercícios muito intensos";
            razao = "Nem todo período é igual. Se está se sentindo bem, mantenha sua rotina com ajustes leves.";
            alternativas = List.of(
                "Caminhada moderada",
                "Atividades criativas",
                "Socialização leve"
            );
        }

        return new DadosSugestaoDTO(
                null,
                titulo,
                descricao,
                "Cuidado Menstrual",
                "variável",
                "🌸",
                razao,
                alternativas,
                null,
                null
        );
    }
}
