package account.application.usecase;

import account.adapter.out.persistence.repository.ProcessedEventJpaRepository;
import org.aspectj.weaver.ast.Call;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProcessTransferEventConcurrencyTest {

    @Autowired
    private ProcessTransferEventUseCase useCase;

    @Autowired
    private ProcessedEventJpaRepository repository;

    @Test
    void shouldRegisterOnlyOnceWhenProcessedConcurrently() throws Exception {

        UUID eventId = UUID.randomUUID();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch start = new CountDownLatch(1);

        try {
            Callable<Void> task = () -> {
                start.await();
                useCase.execute(eventId);
                return null;
            };
            Future<Void> first = executor.submit(task);
            Future<Void> second = executor.submit(task);

            start.countDown();

            first.get(15, TimeUnit.SECONDS);
            second.get(15, TimeUnit.SECONDS);

            assertTrue(repository.existsById(eventId));
        } finally {
            executor.shutdown();
            repository.deleteById(eventId);
        }
    }
}
