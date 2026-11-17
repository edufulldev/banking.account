
package account.adapter.out.persistence;

import account.adapter.out.persistence.entity.OutboxEventEntity;
import account.adapter.out.persistence.repository.OutboxEventJpaRepository;
import account.domain.event.TransferCreatedEvent;
import account.domain.port.out.OutboxEventRepositoryPort;

import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class OutboxEventPersistenceAdapter
        implements OutboxEventRepositoryPort {

    private final OutboxEventJpaRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxEventPersistenceAdapter(OutboxEventJpaRepository repository, ObjectMapper objectMapper) {

        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(TransferCreatedEvent event) {

        String payload = objectMapper.writeValueAsString(event);

        OutboxEventEntity entity = new OutboxEventEntity("TransferCreated", payload);

        repository.save(entity);
    }
}
