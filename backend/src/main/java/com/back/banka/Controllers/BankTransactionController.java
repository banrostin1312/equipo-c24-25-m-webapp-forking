package com.back.banka.Controllers;

import com.back.banka.Dtos.RequestDto.TransactionHistoryRequestDto;
import com.back.banka.Dtos.RequestDto.TransactionRequestDto;
import com.back.banka.Services.Impl.BankTransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class BankTransactionController {

    private final BankTransactionServiceImpl transactionService;

    @PostMapping("/sendmoney")
    public ResponseEntity<String> sendMoney(@RequestBody TransactionRequestDto requestDto){
    return transactionService.sendMoney(requestDto);
    }

    @PostMapping("/receivemoney")
    public ResponseEntity<String> receiveMoney(@RequestBody TransactionRequestDto requestDto){
        return transactionService.receiveMoney(requestDto);
    }

    @GetMapping("/transaction-history")
    public ResponseEntity<?> getTransactionHistory(@RequestBody TransactionHistoryRequestDto requestDto){
        return  transactionService.getTransactionHistory(requestDto);
    }
}





