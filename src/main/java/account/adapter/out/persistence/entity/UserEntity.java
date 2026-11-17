package account.adapter.out.persistence.entity;

import account.domain.model.Role;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username")})
public class UserEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    protected UserEntity() {}

    public UserEntity(UUID id, String username, String password, Role role) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
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
