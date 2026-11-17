package account.application.usecase;

import account.domain.model.User;
import account.domain.port.out.UserRepositoryPort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateUserUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(String username, String password) {

        if (username == null || password == null) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Usuário ou senha inválidos"));

        boolean senhaConfere = passwordEncoder.matches(password, user.getPassword());

        // Diagnóstico temporário
        System.out.println("Senha BCrypt confere: " + senhaConfere);

        if (!senhaConfere) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        return user;
    }

}
