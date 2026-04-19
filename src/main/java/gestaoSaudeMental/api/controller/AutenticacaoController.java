package gestaoSaudeMental.api.controller;

import gestaoSaudeMental.api.domain.auth.Credenciais;
import gestaoSaudeMental.api.domain.auth.DadosTokenJWT;
import gestaoSaudeMental.api.domain.usuario.DadosAutenticacao;
import gestaoSaudeMental.api.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authToken);
        var token = tokenService.gerarToken((Credenciais) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(token));
    }
}
