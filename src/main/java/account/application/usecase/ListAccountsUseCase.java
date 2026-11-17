package account.application.usecase;

import account.domain.model.Account;
import account.domain.port.out.AccountRepositoryPort;

import java.util.List;

public class ListAccountsUseCase {

    private final AccountRepositoryPort accountRepository;

    public ListAccountsUseCase(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> execute() {

        return accountRepository.findAll();
    }
}
