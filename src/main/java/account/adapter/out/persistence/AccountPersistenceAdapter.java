package account.adapter.out.persistence;

import account.adapter.out.persistence.entity.AccountEntity;
import account.adapter.out.persistence.repository.AccountJpaRepository;
import account.domain.model.Account;
import account.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountPersistenceAdapter implements AccountRepositoryPort {

    private final AccountJpaRepository jpaRepository;

    public AccountPersistenceAdapter(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Account save(Account account) {

        AccountEntity entity = new AccountEntity(
                account.getId(),
                account.getAccountNumber(),
                account.getHolderName(),
                account.getBalance(),
                account.getCreatedAt()
        );
        AccountEntity saveEntity = jpaRepository.save(entity);

        return toDomain(saveEntity);
    }

    @Override
    public Optional<Account> findById(UUID id) {

        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {

        return jpaRepository.existsByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Account toDomain(AccountEntity entity) {

        return Account.restore(
                entity.getId(),
                entity.getAccountNumber(),
                entity.getHolderName(),
                entity.getBalance(),
                entity.getCreatedAt()
        );
    }
}
