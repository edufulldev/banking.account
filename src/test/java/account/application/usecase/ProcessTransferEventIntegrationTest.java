package account.application.usecase;

import account.adapter.out.persistence.repository.ProcessedEventJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProcessTransferEventIntegrationTest {

    @Autowired
    private ProcessTransferEventUseCase useCase;

    @Autowired
    private ProcessedEventJpaRepository repository;

    @Test
    void shouldNotPersistSameEventTwice() {

        UUID eventId = UUID.randomUUID();

        try {
            useCase.execute(eventId);
            useCase.execute(eventId);

            assertTrue(repository.existsById(eventId));
            assertEquals(1, repository.count());
        } finally {
            repository.deleteById(eventId);
        }
    }
}
