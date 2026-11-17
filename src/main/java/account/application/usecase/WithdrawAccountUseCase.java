package account.application.usecase;

import account.domain.exception.AccountNotFoundException;
import account.domain.model.Account;
import account.domain.model.Transaction;
import account.domain.model.TransactionType;
import account.domain.port.out.AccountRepositoryPort;
import account.domain.port.out.TransactionRepositoryPort;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

public class WithdrawAccountUseCase {

    private final AccountRepositoryPort accountRepository;
    private final TransactionRepositoryPort transactionRepository;

    public WithdrawAccountUseCase(AccountRepositoryPort accountRepository, TransactionRepositoryPort transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Account execute(UUID accountId, BigDecimal amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        // Validar saldo e realizar saque
        account.withdraw(amount);

        // Persistir saldo atualizado
        Account updatedAccount = accountRepository.save(account);

        // Registrar movimentação no extrato
        Transaction transaction = new Transaction(accountId, TransactionType.WITHDRAWAL, amount);

        transactionRepository.save(transaction);

        return updatedAccount;
    }
}
