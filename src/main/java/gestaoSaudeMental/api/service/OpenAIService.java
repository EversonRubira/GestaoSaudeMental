package gestaoSaudeMental.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService implements AIService {

    private final WebClient webClient;

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.api.model:gpt-3.5-turbo}")
    private String model;

    @Value("${ai.enabled:false}")
    private boolean aiEnabled;

    public OpenAIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .build();
    }

    @Override
    public String gerarMensagemMotivacional(String nome, String estadoEmocional, String atividade) {
        // Se AI não está habilitada ou não tem API key, retorna mensagem padrão
        if (!aiEnabled || apiKey == null || apiKey.isEmpty()) {
            return gerarMensagemPadrao(nome, estadoEmocional, atividade);
        }

        try {
            String prompt = String.format(
                "Você é um assistente de saúde mental empático e motivacional. " +
                "Gere uma mensagem curta, personalizada e motivacional (máximo 2 frases) para %s, " +
                "que está se sentindo %s e realizou a atividade: %s. " +
                "A mensagem deve ser calorosa, encorajadora e específica para essa combinação de emoção e atividade.",
                nome, estadoEmocional.toLowerCase(), atividade.toLowerCase()
            );

            Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                    Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 100,
                "temperature", 0.7
            );

            String response = webClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(10))
                    .map(this::extrairMensagem)
                    .onErrorResume(e -> {
                        System.err.println("Erro ao chamar OpenAI: " + e.getMessage());
                        return Mono.just(gerarMensagemPadrao(nome, estadoEmocional, atividade));
                    })
                    .block();

            return response != null ? response : gerarMensagemPadrao(nome, estadoEmocional, atividade);

        } catch (Exception e) {
            System.err.println("Erro ao gerar mensagem com IA: " + e.getMessage());
            return gerarMensagemPadrao(nome, estadoEmocional, atividade);
        }
    }

    private String extrairMensagem(Map<String, Object> response) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }
        } catch (Exception e) {
            System.err.println("Erro ao extrair mensagem da resposta: " + e.getMessage());
        }
        return null;
    }

    private String gerarMensagemPadrao(String nome, String estadoEmocional, String atividade) {
        return String.format(
            "%s, você está fazendo um ótimo trabalho cuidando da sua saúde mental! Continue assim.",
            nome
        );
    }
}
