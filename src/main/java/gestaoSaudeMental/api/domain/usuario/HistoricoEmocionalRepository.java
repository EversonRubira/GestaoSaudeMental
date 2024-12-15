package gestaoSaudeMental.api.domain.usuario;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HistoricoEmocionalRepository extends JpaRepository<HistoricoEmocional, Long> {
    List<HistoricoEmocional> findByUsuarioIdAndDataRegistroBetween(Long usuarioId, LocalDate inicio, LocalDate fim);
    List<HistoricoEmocional> findByUsuarioIdOrderByDataRegistroAsc(Long usuarioId);
    List<HistoricoEmocional> findByUsuarioIdAndEstadoEmocionalOrderByDataRegistroAsc(Long usuarioId, EstadoEmocionalEnum estadoEmocional);


}
