package gestaoSaudeMental.api.domain.usuario;

public record DadosTokenDTO(String token, String type, Long expiresIn) {
    public DadosTokenDTO(String token, Long expiresIn) {
        this(token, "Bearer", expiresIn);
    }
}
