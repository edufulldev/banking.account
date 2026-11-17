package account.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(

        @NotBlank(message = "O número da conta é obrigatório")
        String accountNumber,

        @NotBlank(message = "O nome do titular é obrigatório")
        String holderName
) {
}
