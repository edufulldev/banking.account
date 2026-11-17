package account.adapter.in.streams;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@Configuration
public class TransferStreamProcessor {

    private static final Logger log = LoggerFactory.getLogger(TransferStreamProcessor.class);

    @Bean
    public KStream<String, String> transferStream(StreamsBuilder streamsBuilder, ObjectMapper objectMapper) {

        log.info(">>> TRANSFER STREAM PROCESSOR FOI INICIALIZADO <<<");

        KStream<String, String> stream = streamsBuilder.stream("banking.transfers");

        stream.peek((key, value) -> {
            log.info(">>> EVENTO RECEBIDO PELO STREAMS | chave={} | valor={}", key, value);
        });

        KStream<String, String> filteredStream = stream.filter((key, value) -> {
            JsonNode json = objectMapper.readTree(value);
            BigDecimal amount = new BigDecimal(json.get("amount").asText());

            return amount.compareTo(new BigDecimal("20.00")) >= 0;
        });

        filteredStream.foreach((key, value) -> {
            log.info("Transferência >= R$ 20 identificada | chave={} | evento={}", key, value);
        });

        return stream;
    }
}
