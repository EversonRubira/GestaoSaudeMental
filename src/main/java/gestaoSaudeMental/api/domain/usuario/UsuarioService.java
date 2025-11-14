package gestaoSaudeMental.api.domain.usuario;

import gestaoSaudeMental.api.domain.auth.Credenciais;
import gestaoSaudeMental.api.domain.auth.CredenciaisRepository;
import gestaoSaudeMental.api.infra.exception.EmailJaExisteException;
import gestaoSaudeMental.api.infra.exception.UsuarioNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CredenciaisRepository credenciaisRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public DadosUsuarioCriadoDTO cadastrarUsuario(DadosCadastroUsuario dados) {
        log.info("Iniciando cadastro de usuário: {}", dados.getEmail());

        // Validar email único
        if (credenciaisRepository.findByUsuario_Email(dados.getEmail()).isPresent()) {
            log.error("Tentativa de cadastro com email já existente: {}", dados.getEmail());
            throw new EmailJaExisteException(dados.getEmail());
        }

        // Criar usuário
        var usuario = new Usuario(
                null,
                dados.getNome(),
                dados.getEmail(),
                dados.getTelefone(),
                dados.getDataNascimento(),
                true,
                null,
                null,
                dados.getGenero()
        );
        usuarioRepository.save(usuario);
        log.info("Usuário criado com ID: {}", usuario.getId());

        // Criar credenciais
        String senhaHash = passwordEncoder.encode(dados.getSenha());
        var credenciais = new Credenciais(null, usuario, senhaHash);
        credenciaisRepository.save(credenciais);
        log.info("Credenciais criadas para o usuário: {}", dados.getEmail());

        return new DadosUsuarioCriadoDTO(usuario.getId(), usuario.getNome());
    }

    @Transactional
    public void desativarUsuario(Long id) {
        log.info("Desativando usuário com ID: {}", id);
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
        log.info("Usuário desativado com sucesso: ID {}", id);
    }

    public Usuario buscarUsuarioPorId(Long id) {
        log.debug("Buscando usuário por ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    public boolean emailExiste(String email) {
        return credenciaisRepository.findByUsuario_Email(email).isPresent();
    }
}
