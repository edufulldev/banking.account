package account.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private final UUID id;
    private final UUID accountId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final LocalDateTime createdAt;

    public Transaction(UUID accountId, TransactionType type, BigDecimal amount) {

        this(
                UUID.randomUUID(),
                accountId,
                type,
                amount,
                LocalDateTime.now()
        );
    }

    private Transaction(
            UUID id,
            UUID accountId,
            TransactionType type,
            BigDecimal amount,
            LocalDateTime createdAt
    ){
        this.id = Objects.requireNonNull(id, "ID obrigatório");
        this.accountId = Objects.requireNonNull(accountId, "Conta obrigatória");
        this.type = Objects.requireNonNull(type, "Tipo de movimentação obrigatório");

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("O valor da movimentação deve ser maior que zero");
        }

        this.amount = amount;
        this.createdAt = Objects.requireNonNull(createdAt, "Data obrigatória");
    }

    public static Transaction restore(
            UUID id,
            UUID accountId,
            TransactionType type,
            BigDecimal amount,
            LocalDateTime createdAt
    ) {
        return new Transaction(id, accountId, type, amount, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
