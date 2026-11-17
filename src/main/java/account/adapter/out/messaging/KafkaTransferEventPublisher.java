
package account.adapter.out.messaging;

import account.domain.event.TransferCreatedEvent;
import account.domain.port.out.TransferEventPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

@Component
public class KafkaTransferEventPublisher implements TransferEventPublisher {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaTransferEventPublisher.class);

    private static final String TOPIC = "banking.transfers";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaTransferEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(TransferCreatedEvent event) {

        String message = objectMapper.writeValueAsString(event);

        kafkaTemplate.send(TOPIC, event.transferId().toString(), message
        ).whenComplete((result, exception) -> {

            if (exception != null) {log.error(
                        "Falha ao publicar evento de transferência. eventId={}",
                        event.transferId(), exception);
                return;
            }

            log.info(
                    "Evento publicado no Kafka. eventId={}, topic={}, partition={}, offset={}",
                    event.transferId(),
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset()
            );
        });
    }
}
