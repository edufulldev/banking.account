package account.adapter.in.web.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(

        @NotBlank(message = "Username obrigatório")
        @Size(min = 3, max = 100, message = "Username deve ter entre 3 e 100 caracteres")
        String username,

        @NotBlank(message = "Senha obrigatória")
        @Size(min = 8, max = 72, message = "Senha deve ter entre 8 e 72 caracteres")
        String password

) {
}
