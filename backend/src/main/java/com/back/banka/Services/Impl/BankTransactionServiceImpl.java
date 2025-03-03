package com.back.banka.Services.Impl;

import com.back.banka.Dtos.ResponseDto.SendMoneyResponseDto;
import com.back.banka.Dtos.RequestDto.TransactionHistoryRequestDto;
import com.back.banka.Dtos.RequestDto.TransactionRequestDto;
import com.back.banka.Dtos.ResponseDto.DeactivateAccountResponseDto;
import com.back.banka.Enums.AccountStatus;
import com.back.banka.Enums.StatusTransactions;
import com.back.banka.Enums.TransactionType;
import com.back.banka.Exceptions.Custom.BadRequestExceptions;
import com.back.banka.Exceptions.Custom.UserNotFoundException;
import com.back.banka.Model.AccountBank;
import com.back.banka.Model.BankTransaction;
import com.back.banka.Repository.BankTransactionRepository;
import com.back.banka.Repository.IAccountBankRepository;
;
import com.back.banka.Services.IServices.BankTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankTransactionServiceImpl implements BankTransactionService {

    private final BankTransactionRepository transactionRepository;
    private final IAccountBankRepository accountBankRepository;
    private final EmailServiceImpl emailService;

    @Transactional
    public SendMoneyResponseDto sendMoney(Long accountId, TransactionRequestDto requestDto){
        
        AccountBank senderOpt = accountBankRepository.findById(accountId).orElseThrow(()
                -> new UserNotFoundException("Cuenta de envio no encontrada"));
        AccountBank receiver = accountBankRepository.findByNumber(requestDto.getReceiverAccountNumber())
                .orElseThrow(() -> new UserNotFoundException("Cuenta receptora no encontrada"));

        if (senderOpt.getNumber().equals(receiver.getNumber())) {
            throw new BadRequestExceptions("No puedes transferirte dinero a ti mismo.");
        }

        if (senderOpt.getAccountStatus() != AccountStatus.ACTIVE || receiver.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new  BadRequestExceptions("Una o ambas cuentas están inactivas");
        }
        if (requestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new  BadRequestExceptions("El monto de la transferencia debe ser mayor a cero");
        }
        if (senderOpt.getBalance().compareTo(requestDto.getAmount()) < 0){
            throw new  BadRequestExceptions("Saldo insuficiente.");
        }
        senderOpt.setBalance(senderOpt.getBalance().subtract(requestDto.getAmount()));
        receiver.setBalance(receiver.getBalance().add(requestDto.getAmount()));

        accountBankRepository.save(senderOpt);
        accountBankRepository.save(receiver);

        BankTransaction transaction = BankTransaction.builder()
                .accountSend(senderOpt)
                .accountReceiving(receiver)
                .amount(requestDto.getAmount())
                .transactionType(TransactionType.SENDING_TRANSACTION)
                .date(LocalDateTime.now())
                .status(StatusTransactions.COMPLETED)
                .build();

        transactionRepository.save(transaction);


        Map<String, Object> variables = Map.of(
                "amount", requestDto.getAmount(),
                "sender", senderOpt.getUser().getName(),
                "receiver", receiver.getUser().getName(),
                "message", "Has recibido una trasferencia bancaria"
        );
        emailService.sendEmail(senderOpt.getUser().getEmail(),
                "Tu transaccion ha sido enviada",
                "Enviaste transferencia  + "+ variables
                );

        emailService.sendEmail(receiver.getUser().getEmail(),
                "Haz recibido una transferencia",
                "Has recibido " + requestDto.getAmount() + " de la cuenta " + senderOpt.getNumber());

        return SendMoneyResponseDto.builder()
                .receiverAccountNumber(receiver.getNumber())
                .senderAccountNumber(senderOpt.getNumber())
                .amount(requestDto.getAmount())
                .message("Transaccion Realizada con exito")
                .build();
        }


    @Override
    public List<DeactivateAccountResponseDto.TransactionsResponseDto> getTransactionsHistory(Long accountId) {
        AccountBank accountBank = accountBankRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("La cuenta no existe"));

        List<BankTransaction> transactions = transactionRepository
                .findByAccountSendIdOrAccountReceivingIdOrderByDateDesc(accountId, accountId);

        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getDate()),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values().stream()
                .flatMap(List::stream)
                .map(t -> DeactivateAccountResponseDto.TransactionsResponseDto.builder()
                        .date(String.valueOf(t.getDate()))
                        .receiverAccountNumber(t.getAccountReceiving().getNumber())
                        .senderAccountNumber(t.getAccountSend().getNumber())
                        .amount(t.getAmount())
                        .build()
                )
                .toList();
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






