package gestaoSaudeMental.api.domain.usuario;

import jakarta.validation.constraints.NotNull;

public record DadosRegistroEstadoEmocionalDTO(
        @NotNull(message = "O estado emocional é obrigatório.")
        EstadoEmocionalEnum estadoEmocional,

        @NotNull(message = "A atividade realizada é obrigatória.")
        AtividadeRealizadaEnum atividadeRealizada
) {}
