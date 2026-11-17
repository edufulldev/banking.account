package account.application.usecase;

import account.domain.model.Role;
import account.domain.model.User;
import account.domain.port.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User execute(String username, String rawPassword) {

        if(username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username obrigatório");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Senha obrigatória");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username já cadastrado");
        }
        String passwordHash = passwordEncoder.encode(rawPassword);

        User user = new User(
                UUID.randomUUID(),
                username,
                passwordHash,
                Role.ROLE_USER
        );
        return userRepository.save(user);
    }
}
