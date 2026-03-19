package mx.com.invex.springvsquarkus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/spring")
public class SpringTestController {

    private final PythonServiceClient pythonClient;
    private final RestClient restClient = RestClient.create();
    // Inyección de dependencias por constructor (recomendada)
    public SpringTestController(PythonServiceClient pythonClient) {
        this.pythonClient = pythonClient;
    }

    @GetMapping("/pokemon")
    public String fetchPokemon() {
        // Llamada a la PokéAPI (ej. Pikachu)
        return restClient.get()
                .uri("https://pokeapi.co/api/v2/pokemon/25")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/test")
    public CompletableFuture<String> testResilience() {
        return pythonClient.getFlakyData();
    }
    @GetMapping("/stress")
    public String stressTest() {
        double result = 0;

        for (int i = 0; i < 500000; i++) {
            result += Math.sqrt(i);
        }
        return "Spring Boot OK: " + result;
    }

}