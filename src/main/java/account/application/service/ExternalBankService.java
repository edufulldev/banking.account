package account.application.service;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import static org.springframework.data.jpa.repository.query.QueryEnhancerFactories.fallback;

@Service
public class ExternalBankService {

    private final CircuitBreaker circuitBreaker;

    public ExternalBankService(CircuitBreakerFactory<?, ?> factory) {

        this.circuitBreaker = factory.create("externalBank");
    }

    public String checkBankAvailability() {
        return circuitBreaker.run(this::callExternalBank, throwable -> fallback());
    }

    private String callExternalBank() {
        throw new IllegalStateException("Banco externo indisponível");
    }

    private String fallback() {
        return "Serviço bancário externo temporariamente indisponível";
    }
}
