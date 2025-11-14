package gestaoSaudeMental.api.infra.exception;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        Integer status,
        String error,
        String message,
        LocalDateTime timestamp
) {
    public ErrorResponseDTO(Integer status, String error, String message) {
        this(status, error, message, LocalDateTime.now());
    }
}
