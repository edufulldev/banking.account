package account.application.usecase;

import account.domain.exception.AccountNotFoundException;
import account.domain.model.Transaction;
import account.domain.port.out.AccountRepositoryPort;
import account.domain.port.out.TransactionRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public class GetAccountStatementUseCase {

    private final AccountRepositoryPort accountRepository;
    private final TransactionRepositoryPort transactionRepository;

    public GetAccountStatementUseCase(AccountRepositoryPort accountRepository, TransactionRepositoryPort transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public List<Transaction> execute(UUID accountId) {

        if (accountId == null) {
            throw new IllegalArgumentException("O ID da conta é obrigatório");
        }
        if (accountRepository.findById(accountId).isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }
        return transactionRepository.findByAccountId(accountId);
    }
}
