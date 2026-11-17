package account.adapter.in.web.controller;

import account.adapter.in.web.dto.CreateAccountRequest;
import account.adapter.in.web.dto.DepositRequest;
import account.adapter.in.web.dto.TransferRequest;
import account.adapter.in.web.dto.WithdrawRequest;
import account.application.usecase.*;
import account.domain.model.Account;
import account.domain.model.Transaction;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Contas Bancárias", description = "Operações de gerenciamento de contas bancárias")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final ListAccountsUseCase listAccountsUseCase;
    private final DepositAccountUseCase depositAccountUseCase;
    private final WithdrawAccountUseCase withdrawAccountUseCase;
    private final TransferAccountUseCase transferAccountUseCase;
    private final GetAccountStatementUseCase getAccountStatementUseCase;

    public AccountController(
            CreateAccountUseCase createAccountUseCase,
            GetAccountUseCase getAccountUseCase,
            ListAccountsUseCase listAccountsUseCase,
            DepositAccountUseCase depositAccountUseCase,
            WithdrawAccountUseCase withdrawAccountUseCase,
            TransferAccountUseCase transferAccountUseCase, GetAccountStatementUseCase getAccountStatementUseCase) {

        this.createAccountUseCase = createAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.listAccountsUseCase = listAccountsUseCase;
        this.depositAccountUseCase = depositAccountUseCase;
        this.withdrawAccountUseCase = withdrawAccountUseCase;
        this.transferAccountUseCase = transferAccountUseCase;
        this.getAccountStatementUseCase = getAccountStatementUseCase;
    }

    @Operation(
            summary = "Criar conta bancária",
            description = "Cadastra uma nova conta bancária informando o número da conta e o nome do titular."
    )
    @ApiResponse(responseCode = "201", description = "Conta criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {

        Account account = createAccountUseCase.execute(request.accountNumber(), request.holderName());

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }


    @Operation(
            summary = "Realizar depósito",
            description = "Deposita um valor em uma conta bancária.")
    @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Valor de depósito inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "404", description = "Conta bancária não encontrada")

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable UUID id, @Valid @RequestBody DepositRequest request) {

        Account account = depositAccountUseCase.execute(id, request.amount());

        return ResponseEntity.ok(account);
    }

    @Operation(
            summary = "Realizar saque",
            description = "Realiza um saque em uma conta bancária, verificando o saldo disponível.")
    @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Valor inválido ou saldo insuficiente")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "404", description = "Conta bancária não encontrada")

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable UUID id, @Valid @RequestBody WithdrawRequest request) {
        Account account = withdrawAccountUseCase.execute(id, request.amount());

        return ResponseEntity.ok(account);
    }

    @Operation(
            summary = "Transferir entre contas",
            description = "Transfere um valor entre duas contas bancárias, verificando o saldo disponível."
    )
    @ApiResponse(responseCode = "204", description = "Transferência realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Valor inválido ou saldo insuficiente")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "404", description = "Conta de origem ou destino não encontrada")

    @PostMapping("/{id}/transfer")
    public ResponseEntity<Void> transfer(@PathVariable UUID id, @Valid @RequestBody TransferRequest request) {

        transferAccountUseCase.execute(id, request.destinationAccountId(), request.amount());

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Consultar conta bancária",
            description = "Busca uma conta bancária pelo seu identificador UUID."
    )
    @ApiResponse(responseCode = "200", description = "Conta encontrada")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID id) {

        Account account = getAccountUseCase.execute(id);

        return ResponseEntity.ok(account);
    }

    @Operation(
            summary = "Listar contas bancárias",
            description = "Retorna todas as contas bancárias cadastradas no sistema.")
    @ApiResponse(
            responseCode = "200",
            description = "Contas consultadas com sucesso")
    @ApiResponse(
            responseCode = "401",
            description = "Usuário não autenticado")
    @GetMapping
    public ResponseEntity<List<Account>> listAccounts() {

        List<Account> accounts = listAccountsUseCase.execute();

        return ResponseEntity.ok(accounts);
    }


    @Operation(
            summary = "Consultar extrato bancário",
            description = "Retorna o histórico de transações de uma conta bancária."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Extrato consultado com sucesso"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Usuário não autenticado"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Conta não encontrada"
    )
    @GetMapping("/{id}/statement")
    public ResponseEntity<List<Transaction>> getAccountStatement(
            @PathVariable UUID id) {

        List<Transaction> transactions =
                getAccountStatementUseCase.execute(id);

        return ResponseEntity.ok(transactions);
    }

}
