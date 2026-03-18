package mx.com.invex.springvsquarkus;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class PythonServiceClient {

    private static final Logger log = LoggerFactory.getLogger(PythonServiceClient.class);
    private final RestTemplate restTemplate;

    public PythonServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // El atributo "name" conecta estas anotaciones con la configuración en el application.yml
    @Retry(name = "flakyPythonService")
    @CircuitBreaker(name = "flakyPythonService", fallbackMethod = "fallbackResponse")
    @TimeLimiter(name = "flakyPythonService", fallbackMethod = "fallbackResponse")
    public CompletableFuture<String> getFlakyData() {
        log.info("Ejecutando llamada al servicio Python...");

        return CompletableFuture.supplyAsync(() ->
            restTemplate.getForObject("http://localhost:8005/api/flaky-service", String.class)
        );
    }

    // El Fallback DEBE tener la misma firma de retorno y recibir la Excepción
    public CompletableFuture<String> fallbackResponse(Exception e) {
        log.warn("Circuito abierto, timeout o reintentos agotados. Causa: {}", e.getMessage());
        throw new ServiceUnavailableException("Servicio no disponible: " + e.getMessage());
    }
}