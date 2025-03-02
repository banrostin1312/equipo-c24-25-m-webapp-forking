package com.back.banka.Config;
import com.back.banka.Repository.ITokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final ITokenRepository tokenRepository;

    @Autowired
    public CustomLogoutHandler(ITokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido");
        }

        String jwt = authHeader.substring(7);
        var tokens = this.tokenRepository.findByToken(jwt).orElse(null);
        if(tokens != null) {
            tokens.setExpired(true);
            tokens.setRevoked(true);
            this.tokenRepository.save(tokens);
        }

    }
}
