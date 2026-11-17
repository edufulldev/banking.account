package account.adapter.out.messaging;

import account.adapter.out.persistence.entity.OutboxEventEntity;
import account.adapter.out.persistence.repository.OutboxEventJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Component
public class OutboxEventPublisherJob {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventPublisherJob.class);

    private final OutboxEventJpaRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxEventPublisherJob(OutboxEventJpaRepository repository, KafkaTemplate<String, String> kafkaTemplate) {

        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {

        List<OutboxEventEntity> events = repository.findByPublishedFalseOrderByCreatedAtAsc(
                PageRequest.of(0, 50));

        for (OutboxEventEntity event : events) {

            try {
                kafkaTemplate.send(
                        TOPIC,
                        event.getId().toString(),
                        event.getPayload()
                ).get();
                event.markAsPublished();
                repository.save(event);
                log.info("Evento Outbox publicado: {}", event.getId());
            } catch (Exception ex) {
                log.error( "Erro ao publicar evento Outbox: {}", event.getId(), ex);
                break;
            }
        }

    }
}
