package account.application.usecase;

import account.domain.model.Account;
import account.domain.port.out.AccountRepositoryPort;

public class CreateAccountUseCase {

    private final AccountRepositoryPort accountRepository;

    public CreateAccountUseCase(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(String accountNumber, String holderName) {

        if(accountRepository.existsByAccountNumber(accountNumber)) {
            throw new IllegalArgumentException("Já existe uma conta com esse número");
        }
        Account account = new Account(accountNumber, holderName);

        return accountRepository.save(account);
    }
}
