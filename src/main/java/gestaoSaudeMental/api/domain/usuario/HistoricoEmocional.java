package gestaoSaudeMental.api.domain.usuario;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "historico_emocional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class HistoricoEmocional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataRegistro;

    @Enumerated(EnumType.STRING)
    private EstadoEmocionalEnum estadoEmocional;  // Estado emocional do momento

    @Enumerated(EnumType.STRING)
    private AtividadeRealizadaEnum atividadeRealizada;  // Atividade realizada (mantido para compatibilidade)

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_energia")
    private NivelEnergiaEnum nivelEnergia;  // Nível de energia do usuário

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "historico_contextos", joinColumns = @JoinColumn(name = "historico_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "contexto")
    private List<ContextoEnum> contextos;  // Contextos que impactaram o dia

    @ManyToOne
    @JoinColumn(name = "sugestao_id")
    private Sugestao sugestaoRecebida;  // Sugestão que foi dada ao usuário

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_sugestao")
    private FeedbackSugestaoEnum feedbackSugestao;  // Feedback do usuário sobre a sugestão

    @Enumerated(EnumType.STRING)
    @Column(name = "ciclo_menstrual")
    private CicloMenstrualEnum cicloMenstrual;  // Ciclo menstrual (só para mulheres)

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "historico_sintomas", joinColumns = @JoinColumn(name = "historico_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "sintoma")
    private List<SintomaFisicoEnum> sintomasFisicos;  // Sintomas físicos relatados

    @ManyToOne
    @JoinColumn(name = "usuario_id")  // Chave estrangeira para o usuário
    private Usuario usuario;
}
