package account.config;

import account.application.usecase.*;
import account.domain.port.out.AccountRepositoryPort;
import account.domain.port.out.TransactionRepositoryPort;
import account.domain.port.out.UserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import account.domain.port.out.OutboxEventRepositoryPort;

@Configuration
public class ApplicationConfig {

    @Bean
    public CreateAccountUseCase createAccountUseCase(AccountRepositoryPort accountRepository) {

        return new CreateAccountUseCase(accountRepository);
    }

    @Bean
    public GetAccountUseCase getAccountUseCase(AccountRepositoryPort accountRepository) {

        return new GetAccountUseCase(accountRepository);
    }

    @Bean
    public ListAccountsUseCase listAccountsUseCase(AccountRepositoryPort accountRepository) {

        return new ListAccountsUseCase(accountRepository);
    }

    @Bean
    public DepositAccountUseCase depositAccountUseCase(
            AccountRepositoryPort accountRepository,
            TransactionRepositoryPort transactionRepository
    ) {
        return new DepositAccountUseCase(
                accountRepository,
                transactionRepository
        );
    }

    @Bean
    public WithdrawAccountUseCase withdrawAccountUseCase(
            AccountRepositoryPort accountRepository,
            TransactionRepositoryPort transactionRepository) {
        return new WithdrawAccountUseCase(accountRepository, transactionRepository);
    }


    @Bean
    public TransferAccountUseCase transferAccountUseCase(
            AccountRepositoryPort accountRepository,
            TransactionRepositoryPort transactionRepository,
            OutboxEventRepositoryPort outboxEventRepository
    ) {
        return new TransferAccountUseCase(
                accountRepository,
                transactionRepository,
                outboxEventRepository
        );
    }

    @Bean
    public GetAccountStatementUseCase getAccountStatementUseCase(
            AccountRepositoryPort accountRepository,
            TransactionRepositoryPort transactionRepository
    ) {
        return new GetAccountStatementUseCase(accountRepository, transactionRepository);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        return new RegisterUserUseCase(userRepository, passwordEncoder);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        return new AuthenticateUserUseCase(userRepository, passwordEncoder);
    }
}
