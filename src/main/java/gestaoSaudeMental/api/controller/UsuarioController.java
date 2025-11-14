package gestaoSaudeMental.api.controller;

import gestaoSaudeMental.api.domain.usuario.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários e histórico emocional")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HistoricoEmocionalService historicoEmocionalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo usuário", description = "Cria um novo usuário no sistema com suas credenciais")
    public DadosUsuarioCriadoDTO cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        log.info("Requisição de cadastro recebida para o email: {}", dados.getEmail());
        return usuarioService.cadastrarUsuario(dados);
    }

    @PostMapping("/{id}/historico")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Registrar estado emocional", description = "Registra o estado emocional atual do usuário com a atividade realizada")
    public String registrarEstadoEmocional(
            @PathVariable Long id,
            @RequestBody @Valid DadosRegistroEstadoEmocionalDTO dados) {
        log.info("Registrando estado emocional para usuário ID: {}", id);
        return historicoEmocionalService.registrarEstadoEmocional(id, dados);
    }

    @GetMapping("/{id}/historico_por_periodo")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Listar histórico por período", description = "Lista o histórico emocional do usuário em um período específico (padrão: últimos 30 dias)")
    public List<DadosListagemEstadoEmocionalDTO> listarPorPeriodo(
            @PathVariable Long id,
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fim) {
        log.info("Consultando histórico por período para usuário ID: {}", id);
        return historicoEmocionalService.listarPorPeriodo(id, inicio, fim);
    }

    @GetMapping("/{id}/historico-cronologico")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Listar histórico cronológico", description = "Lista todo o histórico emocional do usuário em ordem cronológica, com filtro opcional por emoção")
    public List<DadosListagemEstadoEmocionalDTO> listagemPorEmocao(
            @PathVariable Long id,
            @RequestParam(required = false) EstadoEmocionalEnum emocao) {
        log.info("Consultando histórico cronológico para usuário ID: {}", id);
        return historicoEmocionalService.listarPorEmocao(id, emocao);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Desativar conta", description = "Desativa a conta do usuário (exclusão lógica)")
    public ResponseEntity<Void> excluirConta(@PathVariable Long id) {
        log.info("Desativando conta do usuário ID: {}", id);
        usuarioService.desativarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}




