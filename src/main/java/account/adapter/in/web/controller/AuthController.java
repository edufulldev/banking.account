
package account.adapter.in.web.controller;

import account.adapter.in.web.dto.auth.LoginRequest;
import account.adapter.in.web.dto.auth.LoginResponse;
import account.adapter.in.web.dto.auth.RegisterUserRequest;
import account.adapter.in.web.dto.auth.RegisterUserResponse;
import account.application.service.JwtTokenService;
import account.application.usecase.AuthenticateUserUseCase;
import account.application.usecase.RegisterUserUseCase;
import account.domain.model.User;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Operações de cadastro de usuários e autenticação JWT")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final JwtTokenService jwtTokenService;
    private final long expirationMinutes;

    public AuthController(
            RegisterUserUseCase registerUserUseCase,
            AuthenticateUserUseCase authenticateUserUseCase,
            JwtTokenService jwtTokenService,
            @Value("${security.jwt.expiration-minutes}")
            long expirationMinutes) {

        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.jwtTokenService = jwtTokenService;
        this.expirationMinutes = expirationMinutes;
    }

    @Operation(
            summary = "Registrar usuário",
            description = "Cadastra um novo usuário no sistema com senha protegida por BCrypt."
    )
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "409", description = "Nome de usuário já cadastrado")

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {

        User user = registerUserUseCase.execute(request.username(), request.password());

        RegisterUserResponse response = new RegisterUserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Autenticar usuário",
            description = "Valida as credenciais e retorna um token JWT para acessar endpoints protegidos."
    )
    @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        User user = authenticateUserUseCase.execute(
                request.username(),
                request.password()
        );

        String token = jwtTokenService.generateToken(user);

        LoginResponse response = new LoginResponse(
                token, "Bearer", expirationMinutes * 60);

        return ResponseEntity.ok(response);
    }
}
