package account.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositRequest(

        @NotNull(message = "O valor do depósito é obrigatório")
        @DecimalMin(value = "0.01",
                message = "O valor do depósito deve ser maior que zero")
        @Digits(integer = 17,
                fraction = 2,
                message = "O valor deve ter no máximo duas casas decimais")
        BigDecimal amount
) { }
