
package account.application.usecase;

import account.domain.event.TransferCreatedEvent;
import account.domain.exception.AccountNotFoundException;
import account.domain.model.Account;
import account.domain.model.Transaction;
import account.domain.model.TransactionType;
import account.domain.port.out.AccountRepositoryPort;
import account.domain.port.out.TransactionRepositoryPort;
import account.domain.port.out.OutboxEventRepositoryPort;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransferAccountUseCase {

    private final AccountRepositoryPort accountRepository;
    private final TransactionRepositoryPort transactionRepository;
    private final OutboxEventRepositoryPort outboxEventRepository;

    public TransferAccountUseCase(
            AccountRepositoryPort accountRepository,
            TransactionRepositoryPort transactionRepository,
            OutboxEventRepositoryPort outboxEventRepository
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.outboxEventRepository = outboxEventRepository;
    }

    @Transactional
    public void execute(
            UUID sourceAccountId,
            UUID destinationAccountId,
            BigDecimal amount
    ) {
        if (sourceAccountId == null || destinationAccountId == null) {
            throw new IllegalArgumentException(
                    "As contas de origem e destino são obrigatórias"
            );
        }

        if (sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException(
                    "Não é permitido transferir para a mesma conta"
            );
        }

        Account source = accountRepository.findById(sourceAccountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(sourceAccountId)
                );

        Account destination = accountRepository.findById(destinationAccountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(destinationAccountId)
                );

        // Debitar origem e creditar destino
        source.withdraw(amount);
        destination.deposit(amount);

        // Persistir os saldos
        accountRepository.save(source);
        accountRepository.save(destination);

        // Registrar saida no extrato da origem
        Transaction sentTransaction = new Transaction(
                sourceAccountId,
                TransactionType.TRANSFER_SENT,
                amount
        );

        transactionRepository.save(sentTransaction);

        // Registrar entrada no extrato do destino
        Transaction receivedTransaction = new Transaction(
                destinationAccountId,
                TransactionType.TRANSFER_RECEIVED,
                amount
        );

        transactionRepository.save(receivedTransaction);

        // Criar evento da transferencia
        TransferCreatedEvent event = new TransferCreatedEvent(
                UUID.randomUUID(),
                sourceAccountId,
                destinationAccountId,
                amount,
                LocalDateTime.now()
        );

        // Persistir evento no Outbox na mesma transacao
        outboxEventRepository.save(event);
    }
}
