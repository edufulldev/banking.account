package account.domain.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException{

    public AccountNotFoundException(UUID id) {
        super("Conta bancária não encontrada: " + id);
    }
}
