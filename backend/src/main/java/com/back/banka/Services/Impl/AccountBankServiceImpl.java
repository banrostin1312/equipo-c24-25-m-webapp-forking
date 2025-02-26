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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountBankServiceImpl implements IAccountBankService {

     private final IAccountBankRepository accountBankRepository;
     private final UserRepository userRepository;

    public AccountBankServiceImpl(IAccountBankRepository accountBankRepository, UserRepository userRepository) {
        this.accountBankRepository = accountBankRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ActiveAccountResponseDto activeAccount(ActiveAccountRequestDto requestDto) {
                confirmData(requestDto);
        User user = this.userRepository.findByDNI(requestDto.getDocument()).orElseThrow(()
                -> new BadRequestExceptions(" usuario con documento" + requestDto.getDocument()+ " no existe"));

       AccountBank create =  createBankAccount(user,requestDto);
        AccountBank savedAccount = this.accountBankRepository.save(create);

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

    @Override
    public ActiveAccountResponseDto getBalanceByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestExceptions("Usuario no encontrado"));

        AccountBank account = accountBankRepository.findByUser(user)
                .orElseThrow(() -> new BadRequestExceptions("Cuenta bancaria no encontrada"));

        if (account.getAccountStatus() == AccountStatus.INACTIVE) {
            throw new BadRequestExceptions("Cuenta inactiva. No es posible realizar consultas.");
        }

        return buildAccountResponseDto(account);
    }

}
