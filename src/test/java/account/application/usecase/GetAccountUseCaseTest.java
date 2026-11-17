package account.application.usecase;

import account.domain.exception.AccountNotFoundException;
import account.domain.model.Account;
import account.domain.port.out.AccountRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAccountUseCaseTest {

    private AccountRepositoryPort accountRepository;
    private GetAccountUseCase getAccountUseCase;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepositoryPort.class);
        getAccountUseCase = new GetAccountUseCase(accountRepository);
    }

    @Test
    void shouldGetAccountSuccessfully() {

        Account account = new Account("123456", "Eduardo");

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        Account result = getAccountUseCase.execute(account.getId());

        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        assertEquals("123456", result.getAccountNumber());
        assertEquals("Eduardo", result.getHolderName());

        verify(accountRepository, times(1)).findById(account.getId());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {

        UUID id = UUID.randomUUID();

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> getAccountUseCase.execute(id));

        assertEquals("Conta bancária não encontrada: " + id, exception.getMessage());

        verify(accountRepository, times(1)).findById(id);
    }
}