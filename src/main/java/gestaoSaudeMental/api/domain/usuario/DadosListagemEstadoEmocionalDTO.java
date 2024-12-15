package gestaoSaudeMental.api.domain.usuario;

import java.time.LocalDate;

public record DadosListagemEstadoEmocionalDTO(

        String nome,
        LocalDate dataRegistro,
        EstadoEmocionalEnum estadoEmocional,
        AtividadeRealizadaEnum atividadeRealizada,
        String mensagemMotivacional

) {

    public DadosListagemEstadoEmocionalDTO(HistoricoEmocional historico){
        this(historico.getUsuario().getNome(),
                historico.getDataRegistro(),
                historico.getEstadoEmocional(),
                historico.getAtividadeRealizada(),
                MensagemMotivacional.obterMensagem(historico.getEstadoEmocional(),
                        historico.getAtividadeRealizada(),
                        historico.getUsuario().getNome()));
    }
}
