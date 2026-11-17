package account.domain.port.out;

import account.domain.event.TransferCreatedEvent;

public interface TransferEventPublisher {

    void publish(TransferCreatedEvent event);
}
