package gestaoSaudeMental.api.domain.usuario;

import java.util.List;

public record DadosSolicitacaoSugestaoDTO(
        EstadoEmocionalEnum estadoEmocional,
        NivelEnergiaEnum nivelEnergia,
        List<ContextoEnum> contextos,
        CicloMenstrualEnum cicloMenstrual,
        List<SintomaFisicoEnum> sintomasFisicos
) {
}
