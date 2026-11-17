package account.domain.port.out;

import account.domain.event.TransferCreatedEvent;

public interface OutboxEventRepositoryPort {

    void save(TransferCreatedEvent event);
}
