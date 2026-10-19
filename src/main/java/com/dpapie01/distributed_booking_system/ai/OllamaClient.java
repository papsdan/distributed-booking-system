package com.dpapie01.distributed_booking_system.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Talks to a local Ollama server over HTTP.
 *
 * This class only knows how to send a prompt and get text back. It knows nothing about
 * football or games, which keeps it small and lets AiSearchService be unit tested with a
 * mocked OllamaClient (no real model needed in CI).
 *
 * Ollama runs on your own machine and listens on http://localhost:11434 by default.
 */
@Component
public class OllamaClient {

    private final RestClient restClient;
    private final String model;
    // The app has no JSON HTTP message converters registered (every other endpoint renders
    // Thymeleaf, never JSON), so RestClient's automatic Map/record (de)serialization doesn't
    // work here - we serialize and parse the Ollama payloads ourselves instead.
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public OllamaClient(@Value("${ollama.base-url:http://localhost:11434}") String baseUrl,
                         @Value("${ollama.model:llama3.2}") String model,
                         @Value("${ollama.timeout-seconds:10}") int timeoutSeconds) {
        // Timeouts matter: if the model is slow or Ollama isn't running, we want to give up
        // quickly and fall back to the normal filter form instead of hanging the page.
        // The JDK HttpClient is used here rather than the HttpURLConnection-based
        // SimpleClientHttpRequestFactory, which proved unreliable with Ollama's responses
        // (occasional "no Content-Type" failures on an otherwise valid 200 response).
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(timeoutSeconds));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
        this.model = model;
    }

    /**
     * Sends a system prompt and a user message to Ollama's /api/chat endpoint and returns
     * the model's reply text.
     *
     * @param systemPrompt instructions for the model (what to do and what format to reply in)
     * @param userMessage  what the player typed, e.g. "5-a-side in Leeds on Saturday"
     * @param jsonSchema   the exact JSON shape we want back. Ollama uses it to force the
     *                     model to reply with valid JSON in that shape
     * @return the raw reply text, which should be a JSON object
     */
    public String chat(String systemPrompt, String userMessage, Map<String, Object> jsonSchema) {
        Map<String, Object> request = Map.of(
                "model", model,
                "stream", false,                       // one complete reply, not word by word
                "format", jsonSchema,                   // structured output: reply must match this shape
                // temperature 0 + a fixed seed gets us as close to deterministic as a local
                // quantized model allows - neither fully eliminates run-to-run variance.
                "options", Map.of("temperature", 0, "seed", 42),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userMessage)));

        String responseBody = restClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonMapper.writeValueAsString(request))
                .retrieve()
                .body(String.class);

        OllamaChatResponse response = jsonMapper.readValue(responseBody, OllamaChatResponse.class);
        if (response.message() == null) {
            throw new IllegalStateException("Empty response from Ollama");
        }
        return response.message().content();
    }

    /** The parts of Ollama's reply we care about: {"message": {"content": "..."}} */
    public record OllamaChatResponse(Message message) {
        public record Message(String role, String content) {}
    }
}
