package account.domain.model;

import java.util.Objects;
import java.util.UUID;

public class User {

    private UUID id;
    private final String username;
    private final String password;
    private Role role;

    public User(UUID id, String username, String password, Role role) {
        this.id = Objects.requireNonNull(id, "ID obrigatório");
        this.username = Objects.requireNonNull(username, "Username obrigatório");
        this.password = Objects.requireNonNull(password, "Password obrigatório");
        this.role = Objects.requireNonNull(role, "Role obrigatória");
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
