package account.adapter.out.persistence;

import account.adapter.out.persistence.entity.UserEntity;
import account.adapter.out.persistence.repository.UserJpaRepository;
import account.domain.model.User;
import account.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository repository;

    public UserPersistenceAdapter(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {

        UserEntity entity = new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole()
        );
    }
}
