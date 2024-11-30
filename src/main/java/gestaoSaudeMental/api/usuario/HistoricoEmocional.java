package gestaoSaudeMental.api.usuario;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private AtividadeRealizadaEnum atividadeRealizada;  // Atividade realizada

    @ManyToOne
    @JoinColumn(name = "usuario_id")  // Chave estrangeira para o usuário
    private Usuario usuario;
}
