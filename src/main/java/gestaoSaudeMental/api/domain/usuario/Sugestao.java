package gestaoSaudeMental.api.domain.usuario;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "sugestoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Sugestao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @Column(nullable = false)
    private String categoria;

    private String duracao;

    private String icone;

    @Column(length = 500)
    private String razao;

    @Column(length = 1000)
    private String alternativas;

    @Column(name = "guia_url")
    private String guiaUrl;

    @Column(name = "playlist_url")
    private String playlistUrl;

    // Critérios de matching
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_emocional")
    private EstadoEmocionalEnum estadoEmocional;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_energia")
    private NivelEnergiaEnum nivelEnergia;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sugestao_contextos", joinColumns = @JoinColumn(name = "sugestao_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "contexto")
    private List<ContextoEnum> contextos;

    @Column(nullable = false)
    private Boolean ativa = true;
}
