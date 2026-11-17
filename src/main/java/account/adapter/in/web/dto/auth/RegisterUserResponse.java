package account.adapter.in.web.dto.auth;

import account.domain.model.Role;

import java.util.UUID;

public record RegisterUserResponse(
        UUID id,
        String username,
        Role role
) {
}
