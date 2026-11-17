package account.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawRequest(

        @NotNull(message = "O valor do saque é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor do saque deve ser maior que zero")
        @Digits(integer = 17, fraction = 2, message = "O valor deve possuir no máximo duas casas decimais")

        BigDecimal amount
) {  }
