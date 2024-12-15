package gestaoSaudeMental.api.domain.usuario;


public record DadosRegistroEstadoEmocionalDTO(

        EstadoEmocionalEnum estadoEmocional,
        AtividadeRealizadaEnum atividadeRealizada) {
}
