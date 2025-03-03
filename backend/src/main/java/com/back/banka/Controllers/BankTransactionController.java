package com.back.banka.Controllers;

import com.back.banka.Dtos.ResponseDto.SendMoneyResponseDto;
import com.back.banka.Dtos.RequestDto.TransactionHistoryRequestDto;
import com.back.banka.Dtos.RequestDto.TransactionRequestDto;
import com.back.banka.Dtos.ResponseDto.DeactivateAccountResponseDto;
import com.back.banka.Services.Impl.BankTransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/banca/transacciones")
@RequiredArgsConstructor
public class BankTransactionController {

    private final BankTransactionServiceImpl transactionService;

    @PostMapping("/transferir/{accountID}")
    public ResponseEntity<SendMoneyResponseDto> sendMoney(
            @PathVariable Long accountID,
            @RequestBody TransactionRequestDto requestDto){
        SendMoneyResponseDto responseDto = this.transactionService.sendMoney(accountID,requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/historial-transaccion")
    public ResponseEntity<?> getTransactionHistory(
            @RequestBody TransactionHistoryRequestDto requestDto){
        return  transactionService.getTransactionHistory(requestDto);
    }

    @GetMapping("/historial-transacciones/{accountID}")

    public  ResponseEntity<List<DeactivateAccountResponseDto.TransactionsResponseDto>> getTransactions(
            @RequestParam Long accountID){
        List<DeactivateAccountResponseDto.TransactionsResponseDto> transactionsResponseDtos = this.transactionService.getTransactionsHistory(accountID);
        return ResponseEntity.status(HttpStatus.OK).body(transactionsResponseDtos);
    }
}





