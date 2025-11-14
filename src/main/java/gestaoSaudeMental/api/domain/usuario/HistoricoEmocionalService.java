package gestaoSaudeMental.api.domain.usuario;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HistoricoEmocionalService {

    @Autowired
    private HistoricoEmocionalRepository historicoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public String registrarEstadoEmocional(Long usuarioId, DadosRegistroEstadoEmocionalDTO dados) {
        log.info("Registrando estado emocional para usuário ID: {}", usuarioId);

        var usuario = usuarioService.buscarUsuarioPorId(usuarioId);

        var historico = new HistoricoEmocional(
                null,
                LocalDate.now(),
                dados.estadoEmocional(),
                dados.atividadeRealizada(),
                usuario
        );
        historicoRepository.save(historico);

        log.info("Estado emocional registrado com sucesso para usuário ID: {}", usuarioId);
        return "Registro de estado emocional criado com sucesso para o usuário com ID " + usuarioId + ".";
    }

    public List<DadosListagemEstadoEmocionalDTO> listarPorPeriodo(
            Long usuarioId,
            String inicio,
            String fim
    ) {
        log.info("Listando histórico emocional por período para usuário ID: {}", usuarioId);

        LocalDate dataInicio = (inicio != null) ? LocalDate.parse(inicio) : LocalDate.now().minusDays(30);
        LocalDate dataFim = (fim != null) ? LocalDate.parse(fim) : LocalDate.now();

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser maior que a data de fim.");
        }

        return historicoRepository
                .findByUsuarioIdAndDataRegistroBetween(usuarioId, dataInicio, dataFim)
                .stream()
                .map(DadosListagemEstadoEmocionalDTO::new)
                .collect(Collectors.toList());
    }

    public List<DadosListagemEstadoEmocionalDTO> listarPorEmocao(
            Long usuarioId,
            EstadoEmocionalEnum emocao
    ) {
        log.info("Listando histórico cronológico para usuário ID: {}", usuarioId);

        List<HistoricoEmocional> resultados;

        if (emocao != null) {
            resultados = historicoRepository
                    .findByUsuarioIdAndEstadoEmocionalOrderByDataRegistroAsc(usuarioId, emocao);
        } else {
            resultados = historicoRepository
                    .findByUsuarioIdOrderByDataRegistroAsc(usuarioId);
        }

        return resultados.stream()
                .map(DadosListagemEstadoEmocionalDTO::new)
                .collect(Collectors.toList());
    }
}
