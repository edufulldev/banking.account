package account.application.usecase;

import account.domain.exception.AccountNotFoundException;
import account.domain.model.Account;
import account.domain.model.Transaction;
import account.domain.model.TransactionType;
import account.domain.port.out.AccountRepositoryPort;
import account.domain.port.out.TransactionRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAccountStatementUseCaseTest {

    private AccountRepositoryPort accountRepository;
    private TransactionRepositoryPort transactionRepository;
    private GetAccountStatementUseCase useCase;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepositoryPort.class);
        transactionRepository = mock(TransactionRepositoryPort.class);

        useCase = new GetAccountStatementUseCase(accountRepository, transactionRepository);
    }

    @Test
    void shouldReturnAccountTransactions() {

        Account account = new Account("123456", "Eduardo");

        Transaction deposit = new Transaction(account.getId(), TransactionType.DEPOSIT, new BigDecimal("100.00"));
        Transaction withdrawal = new Transaction(account.getId(), TransactionType.WITHDRAWAL, new BigDecimal("30.00"));

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountId(account.getId())).thenReturn(List.of(withdrawal, deposit));

        List<Transaction> statement = useCase.execute(account.getId());

        assertEquals(2, statement.size());
        assertEquals(TransactionType.WITHDRAWAL, statement.get(0).getType());
        assertEquals(TransactionType.DEPOSIT, statement.get(1).getType());

        verify(transactionRepository).findByAccountId(account.getId());
    }
    @Test
    void shouldReturnEmptyStatementWhenNoTransactionsExist() {

        Account account = new Account("654321", "Maria");

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountId(account.getId())).thenReturn(List.of());

        List<Transaction> statement = useCase.execute(account.getId());

        assertTrue(statement.isEmpty());
    }
    @Test
    void shouldRejectStatementWhenAccountNotFound() {

        UUID accountId = UUID.randomUUID();

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> useCase.execute(accountId));

        verifyNoInteractions(transactionRepository);
    }
    @Test
    void shouldRejectNullAccountId() {

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));

        verifyNoInteractions(accountRepository, transactionRepository);
    }
}