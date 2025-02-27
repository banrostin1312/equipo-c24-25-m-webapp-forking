package com.back.banka.Services.Impl;

import com.back.banka.Dtos.RequestDto.TransactionHistoryRequestDto;
import com.back.banka.Dtos.RequestDto.TransactionRequestDto;
import com.back.banka.Enums.AccountStatus;
import com.back.banka.Enums.StatusTransactions;
import com.back.banka.Enums.TransactionType;
import com.back.banka.Model.AccountBank;
import com.back.banka.Model.BankTransaction;
import com.back.banka.Repository.BankTransactionRepository;
import com.back.banka.Repository.IAccountBankRepository;
;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankTransactionServiceImpl {

    private final BankTransactionRepository transactionRepository;
    private final IAccountBankRepository accountBankRepository;
    private final EmailServiceImpl emailService;

    @Transactional
    public ResponseEntity<String> sendMoney(TransactionRequestDto requestDto){
        Optional<AccountBank> senderOpt = accountBankRepository.findByNumber(requestDto.getSenderAccountNumber());
        Optional<AccountBank> receiverOpt = accountBankRepository.findByNumber(requestDto.getReceiverAccountNumber());

        if(senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("La cuenta del remitente o destinatario no se ha encontrado");
        }

        AccountBank sender = senderOpt.get();
        AccountBank receiver = receiverOpt.get();

        if (sender.getAccountStatus() != AccountStatus.ACTIVE || receiver.getAccountStatus() != AccountStatus.ACTIVE) {
            return ResponseEntity.badRequest().body("Una o ambas cuentas están inactivas");
        }
        if (requestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            return ResponseEntity.badRequest().body("El monto de la transferencia debe ser mayor a cero");
        }
        if (sender.getBalance().compareTo(requestDto.getAmount()) < 0){
            return ResponseEntity.badRequest().body("Saldo insuficiente.");
        }
        sender.setBalance(sender.getBalance().subtract(requestDto.getAmount()));
        receiver.setBalance(receiver.getBalance().add(requestDto.getAmount()));

        accountBankRepository.save(sender);
        accountBankRepository.save(receiver);

        BankTransaction transaction = BankTransaction.builder()
                .accountSend(sender)
                .accountReceiving(receiver)
                .amount(requestDto.getAmount())
                .transactionType(TransactionType.SENDING_TRANSACTION)
                .date(LocalDateTime.now())
                .status(StatusTransactions.COMPLETED)
                .build();

        transactionRepository.save(transaction);


        Map<String, Object> variables = Map.of(
                "amount", requestDto.getAmount(),
                "senderAccount", sender.getNumber()
        );

        emailService.sendEmail(receiver.getUser().getEmail(),
                "Recibiste una transferencia",
                "transferencia-template ",//Debemos tener plantilla llamada "tranferencia-template"
        variables);

        return ResponseEntity.ok("Transferencia exitosa.");
        }

        @Transactional
        public ResponseEntity<String> receiveMoney(TransactionRequestDto requestDto){
        Optional<AccountBank> receiverOpt = accountBankRepository.findByNumber(requestDto.getReceiverAccountNumber());

        if (receiverOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Cuenta receptora no encontrada");
        }

        AccountBank receiver = receiverOpt.get();

        if (receiver.getAccountStatus() != AccountStatus.ACTIVE){
            return ResponseEntity.badRequest().body("La cuenta del remitente está Inactiva");
        }
        if (requestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            return ResponseEntity.badRequest().body("El monto recibido debe ser mayor que cero.");
        }

        receiver.setBalance(receiver.getBalance().add(requestDto.getAmount()));
        accountBankRepository.save(receiver);

        BankTransaction transaction = BankTransaction.builder()
                .accountReceiving(receiver)
                .amount(requestDto.getAmount())
                .transactionType(TransactionType.RECEIVING_TRANSACTION)
                .date(LocalDateTime.now())
                .status(StatusTransactions.COMPLETED)
                .build();

        transactionRepository.save(transaction);

        Map<String, Object> variables = Map.of(
                "amount", requestDto.getAmount(),
                "receiverAccount", receiver.getNumber()

        );

        emailService.sendEmail(receiver.getUser().getEmail(),
                "Has recibido un depósito",
                "deposit-template", //Debemos tener plantilla llamada "deposit-template"
                variables
        );

        return ResponseEntity.ok("Depósito exitoso");
        }



        public ResponseEntity<?> getTransactionHistory(TransactionHistoryRequestDto requestDto) {
            Optional<AccountBank> accountBankOptional = accountBankRepository.findByNumber(requestDto.getAccountNumber());

            if (accountBankOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("La cuenta no fue encontrada");
            }

            AccountBank accountBank = accountBankOptional.get();

            List<BankTransaction> transactions = transactionRepository.findByAccountAndFilters(
                    accountBank,
                    requestDto.getStartDate(),
                    requestDto.getEndDate(),
                    requestDto.getTransactionType()
            );

            return ResponseEntity.ok(transactions);
        }


    }






