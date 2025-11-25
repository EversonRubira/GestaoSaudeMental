package gestaoSaudeMental.api.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SugestaoRepository extends JpaRepository<Sugestao, Long> {

    List<Sugestao> findByEstadoEmocionalAndNivelEnergiaAndAtivaTrue(
            EstadoEmocionalEnum estadoEmocional,
            NivelEnergiaEnum nivelEnergia
    );

    @Query("SELECT s FROM Sugestao s WHERE s.estadoEmocional = :emocao " +
           "AND s.nivelEnergia = :energia " +
           "AND s.ativa = true")
    List<Sugestao> findSugestoesPorEmocaoEEnergia(
            @Param("emocao") EstadoEmocionalEnum emocao,
            @Param("energia") NivelEnergiaEnum energia
    );

    @Query("SELECT COUNT(h) FROM HistoricoEmocional h WHERE h.usuario.id = :usuarioId " +
           "AND h.sugestaoRecebida.id = :sugestaoId " +
           "AND h.feedbackSugestao = 'ACEITA'")
    Long countAceitas(@Param("usuarioId") Long usuarioId, @Param("sugestaoId") Long sugestaoId);

    @Query("SELECT COUNT(h) FROM HistoricoEmocional h WHERE h.usuario.id = :usuarioId " +
           "AND h.sugestaoRecebida.id = :sugestaoId " +
           "AND h.feedbackSugestao = 'RECUSADA'")
    Long countRecusadas(@Param("usuarioId") Long usuarioId, @Param("sugestaoId") Long sugestaoId);
}
