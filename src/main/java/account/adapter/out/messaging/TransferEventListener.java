package account.adapter.out.messaging;

import account.domain.event.TransferCreatedEvent;
import account.domain.port.out.TransferEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TransferEventListener {

    private final TransferEventPublisher eventPublisher;

    public TransferEventListener(TransferEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(TransferCreatedEvent event) {
        eventPublisher.publish(event);
    }
}
