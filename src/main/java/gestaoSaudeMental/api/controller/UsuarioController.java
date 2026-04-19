package gestaoSaudeMental.api.controller;

import gestaoSaudeMental.api.domain.auth.Credenciais;
import gestaoSaudeMental.api.domain.auth.CredenciaisRepository;
import gestaoSaudeMental.api.domain.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private HistoricoEmocionalRepository historicoEmocionalRepository;

    @Autowired
    private CredenciaisRepository credenciaisRepository;

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public DadosUsuarioCriadoDTO cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        var usuario = new Usuario(
                null,
                dados.getNome(),
                dados.getEmail(),
                dados.getTelefone(),
                dados.getDataNascimento(),
                true,
                null,
                null,
                dados.getGenero());
        repository.save(usuario);

        var credenciais = new Credenciais(null, usuario, passwordEncoder.encode(dados.getSenha()));
        credenciaisRepository.save(credenciais);

        return new DadosUsuarioCriadoDTO(usuario.getId(), usuario.getNome());
    }

    @PostMapping("/{id}/historico")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public DadosRegistroHistoricoResponseDTO registrarEstadoEmocional(
            @PathVariable Long id,
            @RequestBody @Valid DadosRegistroEstadoEmocionalDTO dados) {

        var usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        var historico = new HistoricoEmocional(
                null,
                LocalDate.now(),
                dados.estadoEmocional(),
                dados.atividadeRealizada(),
                usuario
        );
        historicoEmocionalRepository.save(historico);

        String mensagem = MensagemMotivacional.obterMensagem(
                dados.estadoEmocional(), dados.atividadeRealizada(), usuario.getNome());

        return new DadosRegistroHistoricoResponseDTO(historico.getId(), mensagem);
    }

    @GetMapping("/{id}/historico_por_periodo")
    public List<DadosListagemEstadoEmocionalDTO> listarPorPeriodo(
            @PathVariable Long id,
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fim) {

        LocalDate dataInicio = (inicio != null) ? LocalDate.parse(inicio) : LocalDate.now().minusDays(30);
        LocalDate dataFim = (fim != null) ? LocalDate.parse(fim) : LocalDate.now();

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser maior que a data de fim.");
        }

        return historicoEmocionalRepository
                .findByUsuarioIdAndDataRegistroBetween(id, dataInicio, dataFim)
                .stream()
                .map(DadosListagemEstadoEmocionalDTO::new)
                .toList();
    }

    @GetMapping("/{id}/historico-cronologico")
    public List<DadosListagemEstadoEmocionalDTO> listagemPorEmocao(
            @PathVariable Long id,
            @RequestParam(required = false) EstadoEmocionalEnum emocao) {

        List<HistoricoEmocional> resultados;
        if (emocao != null) {
            resultados = historicoEmocionalRepository
                    .findByUsuarioIdAndEstadoEmocionalOrderByDataRegistroAsc(id, emocao);
        } else {
            resultados = historicoEmocionalRepository.findByUsuarioIdOrderByDataRegistroAsc(id);
        }

        return resultados.stream()
                .map(DadosListagemEstadoEmocionalDTO::new)
                .toList();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirConta(@PathVariable Long id) {
        var usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        usuario.setAtivo(false);
        repository.save(usuario);
    }
}
