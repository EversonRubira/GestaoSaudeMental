package gestaoSaudeMental.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import gestaoSaudeMental.api.domain.auth.Credenciais;
import gestaoSaudeMental.api.infra.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(Credenciais credenciais) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("gestao-saude-mental-api")
                    .withSubject(credenciais.getUsername())
                    .withClaim("userId", credenciais.getUsuario().getId())
                    .withClaim("nome", credenciais.getUsuario().getNome())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);

            log.info("Token JWT gerado com sucesso para o usuário: {}", credenciais.getUsername());
            return token;
        } catch (JWTCreationException exception) {
            log.error("Erro ao gerar token JWT: {}", exception.getMessage());
            throw new InvalidTokenException("Erro ao gerar token JWT: " + exception.getMessage());
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("gestao-saude-mental-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            log.error("Token JWT inválido ou expirado: {}", exception.getMessage());
            throw new InvalidTokenException("Token JWT inválido ou expirado.");
        }
    }

    public Long getExpirationInMillis() {
        return expiration;
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now()
                .plusSeconds(expiration / 1000)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
