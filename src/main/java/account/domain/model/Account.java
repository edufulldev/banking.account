package account.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Account {

    private final UUID id;
    private final String accountNumber;
    private final String holderName;
    private BigDecimal balance;
    private final LocalDateTime createdAt;

    public Account(String accountNumber, String holderName) {
        this(
                UUID.randomUUID(),
                accountNumber,
                holderName,
                BigDecimal.ZERO,
                LocalDateTime.now()
        );
    }

    private Account(
            UUID id,
            String accountNumber,
            String holderName,
            BigDecimal balance,
            LocalDateTime createdAt
    ){
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("O número da conta é obrigatório");
        }
        if (holderName == null || holderName.isBlank()) {
            throw new IllegalArgumentException("O nome do titular é obrigatório");
        }

        this.id = Objects.requireNonNull(id, "O ID é obrigatório");
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = Objects.requireNonNull(balance, "O saldo é obrigatório");
        this.createdAt = Objects.requireNonNull(createdAt, "A data de criação é obrigatória");
    }

    public static Account restore(
            UUID id,
            String accountNumber,
            String holderName,
            BigDecimal balance,
            LocalDateTime createdAt
    ){
        return new Account(
                id,
                accountNumber,
                holderName,
                balance,
                createdAt
        );
    }

    public UUID getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void deposit(BigDecimal amount) {

        if (amount == null) {
            throw new IllegalArgumentException("O valor do depósito é obrigatório");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser maior que zero");
        }

        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {

        if (amount == null) {
            throw new IllegalArgumentException("O valor do saque é obrigatório");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser maior que zero");
        }

        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o saque");
        }

        this.balance = this.balance.subtract(amount);
    }
}
