package account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

@Configuration
public class JwtAuthenticationConverterConfig {

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken>
    jwtAuthenticationConverter() {

        return jwt -> {

            String role = jwt.getClaimAsString("role");

            List<SimpleGrantedAuthority> authorities =
                    role != null && role.startsWith("ROLE_")
                            ? List.of(new SimpleGrantedAuthority(role))
                            : List.of();

            return new JwtAuthenticationToken(
                    jwt,
                    authorities,
                    jwt.getSubject()
            );
        };
    }
}
