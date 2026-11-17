package account.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_events")
public class ProcessedEventEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime processedAt;

    protected ProcessedEventEntity() {}

    public ProcessedEventEntity(UUID id, LocalDateTime processedAt) {
        this.id = id;
        this.processedAt = processedAt;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
}
