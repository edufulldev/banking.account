package account.adapter.out.persistence;

import account.adapter.out.persistence.entity.TransactionEntity;
import account.adapter.out.persistence.repository.TransactionJpaRepository;
import account.domain.model.Transaction;
import account.domain.port.out.TransactionRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TransactionPersistenceAdapter implements TransactionRepositoryPort {

    private final TransactionJpaRepository jpaRepository;

    public TransactionPersistenceAdapter(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {

        TransactionEntity entity = new TransactionEntity(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCreatedAt()
        );
        TransactionEntity saveEntity = jpaRepository.save(entity);
        return toDomain(saveEntity);
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {

        return jpaRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Transaction toDomain(TransactionEntity entity) {

        return Transaction.restore(
                entity.getId(),
                entity.getAccountId(),
                entity.getType(),
                entity.getAmount(),
                entity.getCreatedAt()
        );
    }
}
