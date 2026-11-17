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

public class DepositAccountUseCase {

    private final AccountRepositoryPort accountRepository;
    private final TransactionRepositoryPort transactionRepository;

    public DepositAccountUseCase(AccountRepositoryPort accountRepository, TransactionRepositoryPort transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Account execute(UUID accountId, BigDecimal amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.deposit(amount);

        Account updatedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction(
                accountId,
                TransactionType.DEPOSIT,
                amount
        );

        transactionRepository.save(transaction);

        return updatedAccount;
    }
}
