package account.application.usecase;

import account.domain.exception.AccountNotFoundException;
import account.domain.model.Account;
import account.domain.port.out.AccountRepositoryPort;

import java.util.UUID;

public class GetAccountUseCase {

    private final AccountRepositoryPort accountRepository;

    public GetAccountUseCase(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(UUID id) {

        return accountRepository.findById(id).orElseThrow(() ->
                new AccountNotFoundException(id));
    }
}
