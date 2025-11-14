package gestaoSaudeMental.api.infra.exception;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }

    public UsuarioNotFoundException(Long id) {
        super("Usuário com ID " + id + " não foi encontrado.");
    }
}
