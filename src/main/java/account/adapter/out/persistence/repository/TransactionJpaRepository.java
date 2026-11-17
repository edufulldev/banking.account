package account.adapter.out.persistence.repository;

import account.adapter.out.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {

    List<TransactionEntity> findByAccountIdOrderByCreatedAtDesc(UUID accountId);
}
