
package account.application.usecase;

import account.domain.exception.AccountNotFoundException;
import account.domain.model.Account;
import account.domain.model.Transaction;
import account.domain.model.TransactionType;
import account.domain.port.out.AccountRepositoryPort;
import account.domain.port.out.OutboxEventRepositoryPort;
import account.domain.port.out.TransactionRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferAccountUseCaseTest {

    private AccountRepositoryPort accountRepository;
    private TransactionRepositoryPort transactionRepository;
    private TransferAccountUseCase transferAccountUseCase;


    @BeforeEach
    void setUp() {

        accountRepository = mock(AccountRepositoryPort.class);
        transactionRepository = mock(TransactionRepositoryPort.class);

        OutboxEventRepositoryPort outboxEventRepository =
                mock(OutboxEventRepositoryPort.class);

        transferAccountUseCase = new TransferAccountUseCase(
                accountRepository,
                transactionRepository,
                outboxEventRepository
        );
    }

    @Test
    void shouldTransferSuccessfully() {

        Account source = new Account("123456", "Eduardo");
        source.deposit(new BigDecimal("100.00"));

        Account destination = new Account("654321", "Maria");

        when(accountRepository.findById(source.getId()))
                .thenReturn(Optional.of(source));

        when(accountRepository.findById(destination.getId()))
                .thenReturn(Optional.of(destination));

        transferAccountUseCase.execute(
                source.getId(),
                destination.getId(),
                new BigDecimal("30.00")
        );

        // Verificar saldos
        assertEquals(
                0,
                new BigDecimal("70.00").compareTo(source.getBalance())
        );

        assertEquals(
                0,
                new BigDecimal("30.00").compareTo(destination.getBalance())
        );

        verify(accountRepository).save(source);
        verify(accountRepository).save(destination);

        // Capturar as duas movimentações
        ArgumentCaptor<Transaction> captor =
                ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository, times(2))
                .save(captor.capture());

        List<Transaction> transactions = captor.getAllValues();

        assertEquals(2, transactions.size());

        Transaction sent = transactions.get(0);
        Transaction received = transactions.get(1);

        // Movimentação de saída
        assertEquals(source.getId(), sent.getAccountId());
        assertEquals(TransactionType.TRANSFER_SENT, sent.getType());

        assertEquals(
                0,
                new BigDecimal("30.00").compareTo(sent.getAmount())
        );

        // Movimentação de entrada
        assertEquals(destination.getId(), received.getAccountId());
        assertEquals(TransactionType.TRANSFER_RECEIVED, received.getType());

        assertEquals(
                0,
                new BigDecimal("30.00").compareTo(received.getAmount())
        );
    }

    @Test
    void shouldRejectTransferWithInsufficientBalance() {

        Account source = new Account("123456", "Eduardo");
        source.deposit(new BigDecimal("70.00"));

        Account destination = new Account("654321", "Maria");

        when(accountRepository.findById(source.getId()))
                .thenReturn(Optional.of(source));

        when(accountRepository.findById(destination.getId()))
                .thenReturn(Optional.of(destination));

        assertThrows(
                IllegalArgumentException.class,
                () -> transferAccountUseCase.execute(
                        source.getId(),
                        destination.getId(),
                        new BigDecimal("200.00")
                )
        );

        verify(accountRepository, never()).save(any(Account.class));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldRejectTransferToSameAccount() {

        Account account = new Account("123456", "Eduardo");

        assertThrows(
                IllegalArgumentException.class,
                () -> transferAccountUseCase.execute(
                        account.getId(),
                        account.getId(),
                        new BigDecimal("30.00")
                )
        );

        verifyNoInteractions(accountRepository);
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldRejectTransferWhenDestinationNotFound() {

        Account source = new Account("123456", "Eduardo");
        UUID destinationId = UUID.randomUUID();

        when(accountRepository.findById(source.getId()))
                .thenReturn(Optional.of(source));

        when(accountRepository.findById(destinationId))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> transferAccountUseCase.execute(
                        source.getId(),
                        destinationId,
                        new BigDecimal("30.00")
                )
        );

        verify(accountRepository, never()).save(any(Account.class));
        verifyNoInteractions(transactionRepository);
    }
}
