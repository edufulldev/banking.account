package account.integration;

import account.application.usecase.WithdrawAccountUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
class WithdrawAccountIntegrationTest {

    @Autowired
    private WithdrawAccountUseCase withdrawAccountUseCase;

    @Autowired
    private AccountRepositoryPort accountRepository;

    @MockitoSpyBean
    private TransactionRepositoryPort transactionRepository;

    @Test
    void shouldWithdrawAndPersistTransaction() {

        // Criar conta no banco de testes
        Account account = accountRepository.save(new Account("WITHDRAW-001", "Eduardo"));

        // Preparar saldo inicial
        account.deposit(new BigDecimal("200.00"));
        accountRepository.save(account);

        // Realizar saque
        withdrawAccountUseCase.execute(account.getId(), new BigDecimal("50.00"));

        // Consultar saldo persistido
        Account updatedAccount = accountRepository.findById(account.getId()).orElseThrow();

        assertEquals(0, new BigDecimal("150.00").compareTo(updatedAccount.getBalance()));

        // Consultar movimentações no PostgreSQL
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        assertEquals(1, transactions.size());

        Transaction transaction = transactions.get(0);

        assertEquals(TransactionType.WITHDRAWAL, transaction.getType());

        assertEquals(account.getId(), transaction.getAccountId());

        assertEquals(0, new BigDecimal("50.00").compareTo(transaction.getAmount()));
    }


    @Test
    void shouldRollbackWhenTransactionSaveFails() {

        // Criar conta com saldo inicial
        Account account = accountRepository.save(new Account("WITHDRAW-ROLLBACK-001", "Eduardo"));

        account.deposit(new BigDecimal("200.00"));
        accountRepository.save(account);

        // Simular falha ao registrar a movimentação
        doThrow(new IllegalStateException("Falha simulada ao registrar saque")).when(transactionRepository).save(any(Transaction.class));

        // Tentar sacar R$ 50,00
        assertThrows(RuntimeException.class, () ->
                withdrawAccountUseCase.execute(account.getId(), new BigDecimal("50.00")));

        // Consultar saldo após a falha
        Account updatedAccount = accountRepository.findById(account.getId()).orElseThrow();

        // O saldo deve permanecer R$ 200,00
        assertEquals(0, new BigDecimal("200.00").compareTo(updatedAccount.getBalance()));

        // Nenhuma movimentação deve ser registrada
        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        assertEquals(0, transactions.size());
    }

}
