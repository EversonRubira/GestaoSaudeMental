package gestaoSaudeMental.api.domain.usuario;

import java.util.List;

public record DadosRegistroComSugestaoDTO(
        EstadoEmocionalEnum estadoEmocional,
        NivelEnergiaEnum nivelEnergia,
        List<ContextoEnum> contextos,
        Long sugestaoId,
        FeedbackSugestaoEnum feedbackSugestao
) {
}
