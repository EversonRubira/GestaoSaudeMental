package gestaoSaudeMental.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DadosErro> tratarRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new DadosErro(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DadosErro> tratarIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new DadosErro(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosCampoInvalido>> tratarErro400(MethodArgumentNotValidException e) {
        var erros = e.getFieldErrors().stream()
                .map(DadosCampoInvalido::new)
                .toList();
        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DadosErro> tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new DadosErro("Credenciais inválidas."));
    }

    public record DadosErro(String mensagem) {}

    public record DadosCampoInvalido(String campo, String mensagem) {
        public DadosCampoInvalido(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
