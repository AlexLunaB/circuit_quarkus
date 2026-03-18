package mx.com.invex.springvsquarkus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/spring")
public class SpringTestController {

    private final PythonServiceClient pythonClient;

    // Inyección de dependencias por constructor (recomendada)
    public SpringTestController(PythonServiceClient pythonClient) {
        this.pythonClient = pythonClient;
    }

    @GetMapping("/test")
    public CompletableFuture<String> testResilience() {
        return pythonClient.getFlakyData();
    }
}