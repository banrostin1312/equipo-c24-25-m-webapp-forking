package com.back.banka.Controllers;

import com.back.banka.Dtos.RequestDto.ActiveAccountRequestDto;
import com.back.banka.Dtos.ResponseDto.ActiveAccountResponseDto;
import com.back.banka.Services.IServices.IAccountBankService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/banca/account")
public class AccountBankController {

    private final IAccountBankService accountBankService;

    @Autowired
    public AccountBankController(IAccountBankService accountBankService) {
        this.accountBankService = accountBankService;
    }

    @PostMapping("/active")
    public ResponseEntity<ActiveAccountResponseDto> activeAccount( @Valid  @RequestBody ActiveAccountRequestDto requestDto){
        ActiveAccountResponseDto activeAccount = this.accountBankService.activeAccount(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(activeAccount);
    }

    @GetMapping("/balance")
    public ResponseEntity<ActiveAccountResponseDto> getBalance(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ActiveAccountResponseDto balance = accountBankService.getBalanceByEmail(email);
        return ResponseEntity.ok(balance);
    }
}
