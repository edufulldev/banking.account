
package account.application.usecase;

import account.domain.exception.AccountNotFoundException;
import account.domain.model.Account;
import account.domain.model.Transaction;
import account.domain.model.TransactionType;
import account.domain.port.out.AccountRepositoryPort;
import account.domain.port.out.TransactionRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DepositAccountUseCaseTest {

    private AccountRepositoryPort accountRepository;
    private TransactionRepositoryPort transactionRepository;
    private DepositAccountUseCase depositAccountUseCase;

    @BeforeEach
    void setUp() {

        accountRepository = mock(AccountRepositoryPort.class);
        transactionRepository = mock(TransactionRepositoryPort.class);

        depositAccountUseCase = new DepositAccountUseCase(
                accountRepository,
                transactionRepository
        );
    }

    @Test
    void shouldDepositSuccessfully() {

        Account account = new Account("123456", "Eduardo");

        when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account result = depositAccountUseCase.execute(
                account.getId(),
                new BigDecimal("150.00")
        );

        assertEquals(
                0,
                new BigDecimal("150.00")
                        .compareTo(result.getBalance())
        );

        verify(accountRepository).findById(account.getId());
        verify(accountRepository).save(account);

        ArgumentCaptor<Transaction> captor =
                ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository).save(captor.capture());

        Transaction transaction = captor.getValue();

        assertEquals(account.getId(), transaction.getAccountId());
        assertEquals(TransactionType.DEPOSIT, transaction.getType());

        assertEquals(
                0,
                new BigDecimal("150.00")
                        .compareTo(transaction.getAmount())
        );
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {

        UUID id = UUID.randomUUID();

        when(accountRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> depositAccountUseCase.execute(
                        id,
                        new BigDecimal("100.00")
                )
        );

        verify(accountRepository).findById(id);
        verify(accountRepository, never()).save(any(Account.class));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldRejectDepositWithNegativeAmount() {

        Account account = new Account("123456", "Eduardo");

        when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.of(account));

        assertThrows(
                IllegalArgumentException.class,
                () -> depositAccountUseCase.execute(
                        account.getId(),
                        new BigDecimal("-50.00")
                )
        );

        verify(accountRepository, never()).save(any(Account.class));
        verifyNoInteractions(transactionRepository);
    }
}
