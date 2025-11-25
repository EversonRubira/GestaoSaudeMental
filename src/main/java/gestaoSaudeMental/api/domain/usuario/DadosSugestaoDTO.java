package gestaoSaudeMental.api.domain.usuario;

import java.util.List;

public record DadosSugestaoDTO(
        Long id,
        String titulo,
        String descricao,
        String categoria,
        String duracao,
        String icone,
        String razao,
        List<String> alternativas,
        String guiaUrl,
        String playlistUrl
) {
    public DadosSugestaoDTO(Sugestao sugestao) {
        this(
                sugestao.getId(),
                sugestao.getTitulo(),
                sugestao.getDescricao(),
                sugestao.getCategoria(),
                sugestao.getDuracao(),
                sugestao.getIcone(),
                sugestao.getRazao(),
                sugestao.getAlternativas() != null ?
                        List.of(sugestao.getAlternativas().split("\\|")) :
                        List.of(),
                sugestao.getGuiaUrl(),
                sugestao.getPlaylistUrl()
        );
    }
}
