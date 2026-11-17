package account.application.usecase;

import account.domain.model.Account;
import account.domain.port.out.AccountRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateAccountUseCaseTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @InjectMocks
    private CreateAccountUseCase createAccountUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAccountSuccessfully() {

        when(accountRepository.existsByAccountNumber("123456")).thenReturn(false);
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account account = createAccountUseCase.execute( "123456", "Eduardo");

        assertNotNull(account);
        assertEquals("123456", account.getAccountNumber());
        assertEquals("Eduardo", account.getHolderName());

        verify(accountRepository, times(1)).save(any(Account.class));
    }
    @Test
    void shouldNotCreateDuplicateAccount() {

        when(accountRepository.existsByAccountNumber("123456")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createAccountUseCase.execute("123456", "Eduardo")
        );

        assertEquals("Já existe uma conta com esse número", exception.getMessage());

        verify(accountRepository, never()).save(any(Account.class));
    }

}