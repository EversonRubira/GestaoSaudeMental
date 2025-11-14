package gestaoSaudeMental.api.infra.exception;

public class EmailJaExisteException extends RuntimeException {
    public EmailJaExisteException(String email) {
        super("O email '" + email + "' já está cadastrado no sistema.");
    }
}
