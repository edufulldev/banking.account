package account.domain.port.out;

import account.domain.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {

    Account save(Account account);
    Optional<Account> findById(UUID id);
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findAll();
}
