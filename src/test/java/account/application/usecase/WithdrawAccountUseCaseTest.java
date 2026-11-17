
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

class WithdrawAccountUseCaseTest {

    private AccountRepositoryPort accountRepository;
    private TransactionRepositoryPort transactionRepository;
    private WithdrawAccountUseCase withdrawAccountUseCase;

    @BeforeEach
    void setUp() {

        accountRepository = mock(AccountRepositoryPort.class);
        transactionRepository = mock(TransactionRepositoryPort.class);

        withdrawAccountUseCase = new WithdrawAccountUseCase(accountRepository, transactionRepository);
    }

    @Test
    void shouldWithdrawSuccessfully() {

        Account account = new Account("123456", "Eduardo");
        account.deposit(new BigDecimal("150.00"));

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account result = withdrawAccountUseCase.execute(account.getId(), new BigDecimal("50.00"));

        // Verificar saldo atualizado
        assertEquals(0, new BigDecimal("100.00").compareTo(result.getBalance()));

        verify(accountRepository).save(account);

        // Capturar movimentação registrada
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository).save(captor.capture());

        Transaction transaction = captor.getValue();

        assertEquals(account.getId(), transaction.getAccountId());
        assertEquals(TransactionType.WITHDRAWAL, transaction.getType());

        assertEquals(0, new BigDecimal("50.00").compareTo(transaction.getAmount()));
    }

    @Test
    void shouldRejectWithdrawWithInsufficientBalance() {

        Account account = new Account("123456", "Eduardo");
        account.deposit(new BigDecimal("100.00"));

        when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () ->
                withdrawAccountUseCase.execute(account.getId(), new BigDecimal("200.00")));

        verify(accountRepository, never()).save(any(Account.class));

        // Não deve registrar movimentação
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {

        UUID id = UUID.randomUUID();

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                withdrawAccountUseCase.execute(id, new BigDecimal("50.00")));

        verify(accountRepository, never()).save(any(Account.class));

        // Não deve registrar movimentação
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void shouldRejectWithdrawWithNegativeAmount() {

        Account account = new Account("123456", "Eduardo");
        account.deposit(new BigDecimal("100.00"));

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () ->
                withdrawAccountUseCase.execute(account.getId(), new BigDecimal("-50.00")));

        verify(accountRepository, never()).save(any(Account.class));
        verifyNoInteractions(transactionRepository);
    }
}
