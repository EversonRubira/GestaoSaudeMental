package gestaoSaudeMental.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SugestaoService {

    @Autowired
    private SugestaoRepository sugestaoRepository;

    /**
     * Gera sugestão personalizada baseada em emoção, energia e contextos
     */
    public DadosSugestaoDTO gerarSugestao(
            EstadoEmocionalEnum emocao,
            NivelEnergiaEnum energia,
            List<ContextoEnum> contextos,
            Long usuarioId
    ) {
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
}
