package gestaoSaudeMental.api.controller;

import gestaoSaudeMental.api.domain.auth.Credenciais;
import gestaoSaudeMental.api.domain.usuario.DadosAutenticacao;
import gestaoSaudeMental.api.domain.usuario.DadosTokenDTO;
import gestaoSaudeMental.api.infra.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    @Operation(summary = "Realizar login", description = "Autentica o usuário e retorna um token JWT")
    public ResponseEntity<DadosTokenDTO> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        log.info("Tentativa de login para o usuário: {}", dados.login());

        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication authentication = manager.authenticate(authenticationToken);

        Credenciais credenciais = (Credenciais) authentication.getPrincipal();
        String tokenJWT = jwtService.generateToken(credenciais);

        log.info("Login realizado com sucesso para o usuário: {}", dados.login());

        return ResponseEntity.ok(new DadosTokenDTO(tokenJWT, jwtService.getExpirationInMillis()));
    }
}
