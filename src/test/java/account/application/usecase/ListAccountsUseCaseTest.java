package account.application.usecase;

import account.domain.model.Account;
import account.domain.port.out.AccountRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListAccountsUseCaseTest {

    private AccountRepositoryPort accountRepository;
    private ListAccountsUseCase listAccountsUseCase;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepositoryPort.class);
        listAccountsUseCase = new ListAccountsUseCase(accountRepository);
    }

    @Test
    void shouldListAllAccounts() {

        Account account1 = new Account("123456", "Eduardo");
        Account account2 = new Account("654321", "Maria");

        when(accountRepository.findAll()).thenReturn(List.of(account1, account2));

        List<Account> result = listAccountsUseCase.execute();

        assertEquals(2, result.size());
        assertEquals("123456", result.get(0).getAccountNumber());
        assertEquals("654321", result.get(1).getAccountNumber());

        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoAccountsExist() {

        when(accountRepository.findAll()).thenReturn(List.of());

        List<Account> result = listAccountsUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(accountRepository, times(1)).findAll();
    }
}