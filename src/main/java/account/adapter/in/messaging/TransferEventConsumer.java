package account.adapter.in.messaging;

import account.application.usecase.ProcessTransferEventUseCase;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransferEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransferEventConsumer.class);
    private final ProcessTransferEventUseCase processTransferEventUseCase;
    private final ObjectMapper objectMapper;

    public TransferEventConsumer(ProcessTransferEventUseCase processTransferEventUseCase, ObjectMapper objectMapper) {
        this.processTransferEventUseCase = processTransferEventUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "banking.transfers" , groupId = "banking-account-group")
    public void consume(String message) {

        JsonNode json = objectMapper.readTree(message);
        UUID eventId = UUID.fromString(json.get("transferId").asText());

        processTransferEventUseCase.execute(eventId);
        log.info("Evento de transferência recebido: {}", eventId);
    }
}
