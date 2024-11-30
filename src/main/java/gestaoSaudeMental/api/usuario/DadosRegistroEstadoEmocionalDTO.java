package gestaoSaudeMental.api.usuario;


public record DadosRegistroEstadoEmocionalDTO(

        EstadoEmocionalEnum estadoEmocional,
        AtividadeRealizadaEnum atividadeRealizada) {
}
