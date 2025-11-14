package gestaoSaudeMental.api.infra.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException() {
        super("Token JWT inválido ou expirado.");
    }
}
