package account.application.usecase;

import account.adapter.out.persistence.entity.ProcessedEventEntity;
import account.adapter.out.persistence.repository.ProcessedEventJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProcessedEventRegistrationService {

    private final ProcessedEventJpaRepository repository;

    public ProcessedEventRegistrationService(ProcessedEventJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void register(UUID eventId) {

        repository.saveAndFlush(new ProcessedEventEntity(eventId, LocalDateTime.now()));
    }
}
