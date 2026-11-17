package account.integration;

import account.application.usecase.DepositAccountUseCase;
import account.domain.model.Account;
import account.domain.model.Transaction;
import account.domain.model.TransactionType;
import account.domain.port.out.AccountRepositoryPort;
import account.domain.port.out.TransactionRepositoryPort;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
public class DepositAccountIntegrationTest {

    @Autowired
    private DepositAccountUseCase depositAccountUseCase;

    @Autowired
    private AccountRepositoryPort accountRepository;

    @MockitoSpyBean
    private TransactionRepositoryPort transactionRepository;

    @Test
    void shouldDepositAndPersistTransaction() {

        // Criar uma conta no banco de testes
        Account account = accountRepository.save(new Account("DEP-001", "Eduardo"));

        // Realizar depósito pelo caso de uso
        depositAccountUseCase.execute(account.getId(), new BigDecimal("150.00"));

        // Consultar saldo persistido
        Account updatedAccount = accountRepository.findById(account.getId()).orElseThrow();

        assertEquals(0, new BigDecimal("150.00").compareTo(updatedAccount.getBalance()));

        // Consultar movimentações persistidas
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        assertEquals(1, transactions.size());

        Transaction transaction = transactions.get(0);

        assertEquals(TransactionType.DEPOSIT, transaction.getType());

        assertEquals(account.getId(), transaction.getAccountId());

        assertEquals(0, new BigDecimal("150.00").compareTo(transaction.getAmount()));
    }

    @Test
    void shouldRollbackWhenTransactionSaveFails() {

        // Criar conta com saldo inicial zero
        Account account = accountRepository.save(new Account("DEP-ROLLBACK-001", "Eduardo"));

        // Simular falha ao registrar a movimentação
        doThrow(new IllegalStateException("Falha simulada ao registrar movimentação"))
                .when(transactionRepository).save(any(Transaction.class));

        // Tentar realizar o depósito
        assertThrows(RuntimeException.class, () ->
                depositAccountUseCase.execute(account.getId(), new BigDecimal("150.00")));

        // Consultar o saldo persistido após a falha
        Account updatedAccount = accountRepository.findById(account.getId()).orElseThrow();

        // O saldo deve continuar zero
        assertEquals(0, BigDecimal.ZERO.compareTo(updatedAccount.getBalance()));

        // Nenhuma movimentação deve ter sido registrada
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        assertEquals(0, transactions.size());
    }

}
