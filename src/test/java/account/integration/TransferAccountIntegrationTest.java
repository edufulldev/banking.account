package account.integration;

import account.application.usecase.TransferAccountUseCase;
import account.domain.model.Account;
import account.domain.port.out.AccountRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.assertThrows;

import account.domain.model.Transaction;
import account.domain.model.TransactionType;
import account.domain.port.out.TransactionRepositoryPort;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.any;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class TransferAccountIntegrationTest {

    @Autowired
    private TransferAccountUseCase transferAccountUseCase;

    @MockitoSpyBean
    private TransactionRepositoryPort transactionRepository;

    @MockitoSpyBean
    private AccountRepositoryPort accountRepository;


    @Test
    void shouldTransferAndPersistBalances() {

        Account source = accountRepository.save(
                new Account("TEST-001", "Eduardo")
        );

        Account destination = accountRepository.save(
                new Account("TEST-002", "Maria")
        );

        // Preparar saldo inicial
        source.deposit(new BigDecimal("100.00"));
        accountRepository.save(source);

        // Executar transferência
        transferAccountUseCase.execute(
                source.getId(),
                destination.getId(),
                new BigDecimal("30.00")
        );

        // Verificar saldos no PostgreSQL
        Account updatedSource = accountRepository
                .findById(source.getId())
                .orElseThrow();

        Account updatedDestination = accountRepository
                .findById(destination.getId())
                .orElseThrow();

        assertEquals(
                0,
                new BigDecimal("70.00")
                        .compareTo(updatedSource.getBalance())
        );

        assertEquals(
                0,
                new BigDecimal("30.00")
                        .compareTo(updatedDestination.getBalance())
        );

        // Verificar extrato da conta de origem
        List<Transaction> sourceTransactions =
                transactionRepository.findByAccountId(source.getId());

        assertEquals(1, sourceTransactions.size());

        Transaction sent = sourceTransactions.get(0);

        assertEquals(TransactionType.TRANSFER_SENT, sent.getType());
        assertEquals(source.getId(), sent.getAccountId());

        assertEquals(
                0,
                new BigDecimal("30.00").compareTo(sent.getAmount())
        );

        // Verificar extrato da conta de destino
        List<Transaction> destinationTransactions =
                transactionRepository.findByAccountId(destination.getId());

        assertEquals(1, destinationTransactions.size());

        Transaction received = destinationTransactions.get(0);

        assertEquals(TransactionType.TRANSFER_RECEIVED, received.getType());
        assertEquals(destination.getId(), received.getAccountId());

        assertEquals(
                0,
                new BigDecimal("30.00").compareTo(received.getAmount())
        );
    }

    @Test
    void shouldRollbackWhenReceivedTransactionSaveFails() {

        // Criar contas
        Account source = accountRepository.save(
                new Account("ROLLBACK-TX-001", "Eduardo")
        );

        Account destination = accountRepository.save(
                new Account("ROLLBACK-TX-002", "Maria")
        );

        // Preparar saldo inicial
        source.deposit(new BigDecimal("100.00"));
        accountRepository.save(source);

        // Simular falha ao salvar a movimentação recebida
        doAnswer(invocation -> {

            Transaction transaction = invocation.getArgument(0);

            if (transaction.getType() == TransactionType.TRANSFER_RECEIVED) {
                throw new IllegalStateException(
                        "Falha simulada ao registrar transferência recebida"
                );
            }

            return invocation.callRealMethod();

        }).when(transactionRepository).save(any(Transaction.class));

        // Executar transferência que deve falhar
        assertThrows(RuntimeException.class, () ->
                transferAccountUseCase.execute(
                        source.getId(),
                        destination.getId(),
                        new BigDecimal("30.00")
                )
        );

        // Consultar saldos persistidos
        Account updatedSource = accountRepository
                .findById(source.getId())
                .orElseThrow();

        Account updatedDestination = accountRepository
                .findById(destination.getId())
                .orElseThrow();

        // Verificar rollback dos saldos
        assertEquals(
                0,
                new BigDecimal("100.00")
                        .compareTo(updatedSource.getBalance())
        );

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(updatedDestination.getBalance())
        );

        // Verificar rollback das movimentações
        List<Transaction> sourceTransactions =
                transactionRepository.findByAccountId(source.getId());

        List<Transaction> destinationTransactions =
                transactionRepository.findByAccountId(destination.getId());

        assertTrue(sourceTransactions.isEmpty());
        assertTrue(destinationTransactions.isEmpty());
    }


}
