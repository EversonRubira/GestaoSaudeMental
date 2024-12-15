package gestaoSaudeMental.api.controller;

import gestaoSaudeMental.api.domain.usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private HistoricoEmocionalRepository historicoEmocionalRepository;

//cadastro
    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public DadosUsuarioCriadoDTO cadastrar(@org.jetbrains.annotations.NotNull @RequestBody DadosCadastroUsuario dados) {
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

        return new DadosUsuarioCriadoDTO(usuario.getId(), usuario.getNome());
    }
//atualizacao dos dados emocionais pelo usuario
    @PostMapping("/{id}/historico")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public String registrarEstadoEmocional(
            @PathVariable Long id,
            @RequestBody DadosRegistroEstadoEmocionalDTO dados) {
         var usuario = repository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Usuario nao Cadastrado"));
         var historico = new HistoricoEmocional(
                 null,
                 LocalDate.now(),
                 dados.estadoEmocional(),
                 dados.atividadeRealizada(),
                 usuario
         );
         historicoEmocionalRepository.save(historico);

        // Obter mensagem motivacional
        String mensagem = MensagemMotivacional.obterMensagem(dados.estadoEmocional(), dados.atividadeRealizada(), usuario.getNome());

         return "Registro de estado emocional criado com sucesso para o usuário com ID " + id + ".";
    }

    //Fornece flexibilidade para o cliente buscar o histórico emocional de um período específico.
    @GetMapping("/{id}/historico_por_periodo")
    public List<DadosListagemEstadoEmocionalDTO> listarPorPeriodo(
            @PathVariable Long id,
            @RequestParam (required = false) String inicio,
            @RequestParam (required = false) String fim) {
        LocalDate dataInicio = (inicio != null) ? LocalDate.parse(inicio) : LocalDate.now().minusDays(30);
        LocalDate dataFim = (fim != null) ? LocalDate.parse(fim) : LocalDate.now();
        if(dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser maior que a data de fim.");
        }
        return historicoEmocionalRepository
                .findByUsuarioIdAndDataRegistroBetween(id, dataInicio, dataFim)
                .stream()
                .map(DadosListagemEstadoEmocionalDTO::new)
                .toList();
    }

    //Fornece uma visão completa por emocao
    @GetMapping("/{id}/historico-cronologico")
    public List<DadosListagemEstadoEmocionalDTO> listagemPorEmocao(
            @PathVariable Long id,
            @RequestParam (required = false) EstadoEmocionalEnum emocao) {
        List<HistoricoEmocional> resultados;

        if (emocao != null) {
            // Filtrar por usuário e emoção
            resultados = historicoEmocionalRepository.findByUsuarioIdAndEstadoEmocionalOrderByDataRegistroAsc(id, emocao);
        } else {
            // Listar todo o histórico cronológico
            resultados = historicoEmocionalRepository.findByUsuarioIdOrderByDataRegistroAsc(id);
        }

        return resultados.stream()
                .map(DadosListagemEstadoEmocionalDTO::new)
                .toList();
    }

    @DeleteMapping
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String excluirConta(@PathVariable Long id) {
        var usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado."));

        usuario.setAtivo(false);
        repository.save(usuario);

        return "Usuário com ID " + id + " foi desativado com sucesso.";

    }

}




