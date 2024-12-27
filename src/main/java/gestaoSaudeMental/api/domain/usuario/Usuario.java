package gestaoSaudeMental.api.domain.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    //@Email
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    @Column(name = "ativo")
    private boolean ativo = true;
    @Enumerated(EnumType.STRING)
    private EstadoEmocionalEnum estadoEmocional;
    @Enumerated(EnumType.STRING)
    private AtividadeRealizadaEnum atividadeRealizada;
    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;
}
