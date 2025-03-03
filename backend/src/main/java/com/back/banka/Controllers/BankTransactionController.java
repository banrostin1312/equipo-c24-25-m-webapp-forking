package com.back.banka.Controllers;

import com.back.banka.Dtos.RequestDto.TransactionRequestDto;
import com.back.banka.Dtos.ResponseDto.TransactionResponseDto;
import com.back.banka.Services.Impl.BankTransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bank/transactions")
@RequiredArgsConstructor
public class BankTransactionController {
    private final BankTransactionServiceImpl transactionService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transfer(@RequestBody TransactionRequestDto requestDto){
        TransactionResponseDto responseDto = transactionService.transfer(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-transactions")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionHistory(){
        List<TransactionResponseDto> transactions = transactionService.getTransactionHistory();
        return ResponseEntity.ok(transactions);
    }

}








