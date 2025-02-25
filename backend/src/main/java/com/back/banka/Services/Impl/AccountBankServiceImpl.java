package com.back.banka.Services.Impl;

import com.back.banka.Dtos.RequestDto.ActiveAccountRequestDto;
import com.back.banka.Dtos.ResponseDto.ActiveAccountResponseDto;
import com.back.banka.Enums.AccountStatus;
import com.back.banka.Enums.AccountType;
import com.back.banka.Exceptions.Custom.BadRequestExceptions;
import com.back.banka.Model.AccountBank;
import com.back.banka.Model.User;
import com.back.banka.Repository.IAccountBankRepository;
import com.back.banka.Repository.UserRepository;
import com.back.banka.Services.IServices.IAccountBankService;
import com.back.banka.Services.IServices.IEmailService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountBankServiceImpl implements IAccountBankService {

     private final IAccountBankRepository accountBankRepository;
     private final UserRepository userRepository;
     private final IEmailService emailService;

    public AccountBankServiceImpl(IAccountBankRepository accountBankRepository, UserRepository userRepository, IEmailService emailService) {
        this.accountBankRepository = accountBankRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
    /**
     * Activa una nueva cuenta bancaria para un usuario.
     * - Si el usuario no existe, lanza una excepción.
     * - Si el usuario ya tiene 3 cuentas bancarias, no permite crear más.
     * - Si es la primera cuenta del usuario, valida la información antes de crearla.
     * - Guarda la nueva cuenta en la base de datos y devuelve la información de la cuenta creada.
     * envia un mensaje al correo electronico del usuario confirmando la creacion de su cuenta bancaria
     *
     * @param requestDto Datos de la solicitud de activación de cuenta.
     * @return ActiveAccountResponseDto con los detalles de la cuenta creada.
     * @throws BadRequestExceptions si el usuario no existe, ya tiene 3 cuentas o los datos no son válidos.
     */

    @Override
    public ActiveAccountResponseDto activeAccount(ActiveAccountRequestDto requestDto) {

        User user = this.userRepository.findByDNI(requestDto.getDocument()).orElseThrow(()
                -> new BadRequestExceptions(" usuario con documento" + requestDto.getDocument()+ " no existe"));
     int accountCount = this.accountBankRepository.countByUser(user);

     if(accountCount > 3){
         throw new BadRequestExceptions("no puede crear mas de 3 cuentas Bancarias");
     }

     if(accountCount == 0){
        confirmData(requestDto);
     }

       AccountBank create =  createBankAccount(user,requestDto);
        AccountBank savedAccount = this.accountBankRepository.save(create);
//        emailService.sendEmail();

        return buildAccountResponseDto(savedAccount);
    }

    private void confirmData(ActiveAccountRequestDto requestDto){
        if(requestDto == null){
            throw new BadRequestExceptions("No puede estar vacio");
        }

        int age = Period.between(requestDto.getBirthDate(), LocalDate.now()).getYears();

        if (age < 18 || age > 120) {
            throw new BadRequestExceptions("Edad inválida. Debe ser mayor de edad y menor de 120 años.");
        }



    }

    private AccountBank createBankAccount(User user, ActiveAccountRequestDto requestDto){

        return AccountBank.builder()
                .accountStatus(AccountStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .type(AccountType.SAVINGS_ACCOUNT)
                .permissionPhrase(requestDto.getSecurityPhrase())
                .number(generateAccountNumber())
                .user(user)
                .build();
    }

    private String generateAccountNumber() {
        String prefix = "58";
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String accountNumber;

        int attempts = 0;
        final int MAX_ATTEMPTS = 6;

        do {
            String randomPart = String.format("%010d", random.nextLong(10_000_000_000L));
            accountNumber = prefix + randomPart;
            attempts++;

            if (attempts >= MAX_ATTEMPTS) {
                throw new RuntimeException("No se pudo generar un número de cuenta único después de " + MAX_ATTEMPTS + " intentos");
            }
        } while (accountBankRepository.existsByNumber(accountNumber));

        return accountNumber;
    }

    private ActiveAccountResponseDto buildAccountResponseDto(AccountBank accountBank){
        return  ActiveAccountResponseDto.builder()
                .accountNumber(accountBank.getNumber())
                .accountType(accountBank.getType().name())
                .accountStatus(accountBank.getAccountStatus().name())
                .balance(accountBank.getBalance())
                .build();
    }

    @Override
    public ActiveAccountResponseDto deactivateAccount(Integer accountNumber) {

        return null;
    }

    @Override
    public ActiveAccountResponseDto getBalance(Long accountId) {
        return null;
    }
}
