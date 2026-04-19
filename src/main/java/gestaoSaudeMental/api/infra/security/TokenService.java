package gestaoSaudeMental.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import gestaoSaudeMental.api.domain.auth.Credenciais;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Credenciais credenciais) {
        var algoritmo = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("gestaoSaudeMental")
                .withSubject(credenciais.getUsername())
                .withExpiresAt(expiracaoEm2Horas())
                .sign(algoritmo);
    }

    public String getSubject(String token) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("gestaoSaudeMental")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token JWT inválido ou expirado.");
        }
    }

    private Instant expiracaoEm2Horas() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
