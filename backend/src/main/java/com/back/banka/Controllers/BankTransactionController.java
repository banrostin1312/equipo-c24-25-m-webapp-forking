package com.back.banka.Controllers;

import com.back.banka.Dtos.RequestDto.TransactionRequestDto;
import com.back.banka.Dtos.ResponseDto.TransactionResponseDto;
import com.back.banka.Services.Impl.BankTransactionServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('CLIENT')")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/banca/transacciones")
@RequiredArgsConstructor
public class BankTransactionController {
    private final BankTransactionServiceImpl transactionService;


    @PostMapping("/transferir/{accountId}")
    public ResponseEntity<TransactionResponseDto> transfer(
            @RequestParam Long accountId,
            @RequestBody TransactionRequestDto requestDto){
        TransactionResponseDto responseDto = transactionService.transfer(accountId,requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/obtener-transacciones/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionHistory(@PathVariable Long accountId){
        List<TransactionResponseDto> transactions = transactionService.getTransactionHistory(accountId);
        return ResponseEntity.ok(transactions);
    }

}








