package com.back.banka.Utils;

import com.back.banka.Exceptions.Custom.BadRequestExceptions;
import com.back.banka.Exceptions.Custom.CustomAuthenticationException;
import com.back.banka.Model.AccountBank;
import com.back.banka.Model.User;
import com.back.banka.Services.IServices.IEmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UtilsServiceImpl implements IUtilsService {

    private final IEmailService emailService;


    @Override
    public void validateOwnership(AccountBank accountBank,
                                  String username) {
        if (!accountBank.getUser().getEmail().equals(username)) {
            throw new CustomAuthenticationException("Error: no esta autorizado para desactivar esta cuenta");

        }
    }

    @Override
    public void validateAccountStatus(AccountBank accountBank) {
        switch (accountBank.getAccountStatus()) {

            case INACTIVE -> throw new BadRequestExceptions("Error: La cuenta ya ha sido desactivada");
            case BLOCKED -> throw new BadRequestExceptions("Error: Su cuenta esta bloqueda no puede ser desactivada");
            default -> {
            }
        }
    }

    @Override
    public void validateBalanceAccount(AccountBank accountBank) {
        if (accountBank.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestExceptions("Error: su cuenta debe estar en cero");
        }
    }

    @Override
    public String getAuthenticatedUser() {
        Logger logger = LoggerFactory.getLogger(getClass());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.error("No hay autenticación en el contexto de seguridad.");
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            logger.info(" Usuario autenticado: " + ((UserDetails) principal).getUsername());
            return ((UserDetails) principal).getUsername();
        } else {
            logger.error(" El usuario no está autenticado correctamente.");
            return null;
        }
    }

    @Override
    public void sendAccountNotification(User user,
                                        String subject,
                                        String templateName,
                                        String message) {
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("name", user.getName());
        emailVariables.put("message", message);

        emailService.sendEmailTemplate(
                user.getEmail(),
                subject,
                templateName,
                emailVariables
        );
    }
}
