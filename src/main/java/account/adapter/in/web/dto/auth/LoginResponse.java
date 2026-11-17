package account.adapter.in.web.dto.auth;

public record LoginResponse( String accessToken,
                             String tokenType,
                             long expiresIn) {
}
