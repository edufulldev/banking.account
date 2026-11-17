package account.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void shouldCreateAccountSuccessfully() {

        Account account = new Account("123456", "Eduardo");

        assertNotNull(account.getId());
        assertEquals("123456", account.getAccountNumber());
        assertEquals("Eduardo", account.getHolderName());
        assertEquals(0, BigDecimal.ZERO.compareTo(account.getBalance()));
        assertNotNull(account.getCreatedAt());

    }

    @Test
    void shouldNotCreateAccountWithoutAccountNumber() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Account(null, "Eduardo"));

        assertEquals("O número da conta é obrigatório", exception.getMessage());
    }

    @Test
    void shouldNotCreateAccountWithBlankAccountNumber() {

        assertThrows(IllegalArgumentException.class,
                () -> new Account("   ", "Eduardo"));
    }
    @Test
    void shouldNotCreateAccountWithoutHolderName() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Account("123456", null));

        assertEquals("O nome do titular é obrigatório", exception.getMessage());
    }

    @Test
    void shouldNotCreateAccountWithBlankHolderName() {

        assertThrows(IllegalArgumentException.class,
                () -> new Account("123456", "   "));
    }

    @Test
    void shouldDepositSuccessfully() {

        Account account = new Account("123456", "Eduardo");
        account.deposit(new BigDecimal("150.00"));
        assertEquals(0, new BigDecimal("150.00").compareTo(account.getBalance()));
    }

    @Test
    void shouldRejectDepositWithZeroAmount() {

        Account account = new Account("123456", "Eduardo");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> account.deposit(BigDecimal.ZERO));

        assertEquals("O valor do depósito deve ser maior que zero", exception.getMessage());
    }

    @Test
    void shouldRejectDepositWithNegativeAmount() {

        Account account = new Account("123456", "Eduardo");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> account.deposit(new BigDecimal("-50.00")));

        assertEquals("O valor do depósito deve ser maior que zero", exception.getMessage());
    }

    @Test
    void shouldWithdrawSuccessfully() {
        Account account = new Account("123456", "Eduardo");

        account.deposit(new BigDecimal("150.00"));
        account.withdraw(new BigDecimal("50.00"));

        assertEquals(
                0,
                new BigDecimal("100.00")
                        .compareTo(account.getBalance())
        );
    }

    @Test
    void shouldRejectWithdrawWithInsufficientBalance() {
        Account account = new Account("123456", "Eduardo");

        account.deposit(new BigDecimal("150.00"));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new BigDecimal("200.00")));
        assertEquals(0, new BigDecimal("150.00").compareTo(account.getBalance()));
    }

    @Test
    void shouldRejectWithdrawWithZeroAmount() {
        Account account = new Account("123456", "Eduardo");

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(BigDecimal.ZERO));
    }

    @Test
    void shouldRejectWithdrawWithNegativeAmount() {
        Account account = new Account("123456", "Eduardo");

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new BigDecimal("-10.00")));
    }
}