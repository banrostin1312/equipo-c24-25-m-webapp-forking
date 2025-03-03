package com.back.banka.Services.Impl;

import com.back.banka.Dtos.RequestDto.TransactionRequestDto;
import com.back.banka.Dtos.ResponseDto.TransactionResponseDto;
import com.back.banka.Enums.AccountStatus;
import com.back.banka.Enums.StatusTransactions;
import com.back.banka.Enums.TransactionType;
import com.back.banka.Exceptions.Custom.*;
import com.back.banka.Model.AccountBank;
import com.back.banka.Model.BankTransaction;
import com.back.banka.Repository.BankTransactionRepository;
import com.back.banka.Repository.IAccountBankRepository;
import com.back.banka.Utils.IUtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankTransactionServiceImpl {
    private final BankTransactionRepository transactionRepository;
    private final IAccountBankRepository accountBankRepository;
    private final EmailServiceImpl emailService;
    private final IUtilsService utilsService;


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public TransactionResponseDto transfer(Long senderAccountId, TransactionRequestDto requestDto) {
        String username = this.utilsService.getAuthenticatedUser();
        if (username == null) {
            throw new CustomAuthenticationException("Error: usuario no autenticado");
        }

        AccountBank senderAccount = accountBankRepository.findByIdAndAccountStatus(senderAccountId, AccountStatus.ACTIVE)
                .orElseThrow(() -> new ModelNotFoundException("Cuenta bancaria remitente no encontrada o inactiva"));

        this.utilsService.validateOwnership(senderAccount, username);

        AccountBank receiverAccount = accountBankRepository.findByNumber(requestDto.getReceiverAccountNumber())
                .orElseThrow(() -> new ModelNotFoundException("Cuenta de destino no encontrada"));

        if (senderAccount.getId().equals(receiverAccount.getId())) {
            throw new InvalidTransactionsException("No puedes transferir dinero a la misma cuenta");
        }

        if (requestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("El monto a transferir debe ser mayor a cero");
        }

        if (senderAccount.getBalance().compareTo(requestDto.getAmount()) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente para realizar la transferencia");
        }

        if (!senderAccount.getAccountStatus().equals(AccountStatus.ACTIVE) ||
                !receiverAccount.getAccountStatus().equals(AccountStatus.ACTIVE)) {
            throw new AccountStatusException("Ambas cuentas deben estar activas");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(requestDto.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(requestDto.getAmount()));

        List<AccountBank> accountsToUpdate = Arrays.asList(senderAccount, receiverAccount);
        accountBankRepository.saveAll(accountsToUpdate);

        BankTransaction transaction = BankTransaction.builder()
                .accountSend(senderAccount)
                .accountReceiving(receiverAccount)
                .amount(requestDto.getAmount())
                .transactionType(TransactionType.SENDING_TRANSACTION)
                .date(LocalDateTime.now())
                .status(StatusTransactions.COMPLETED)
                .build();

        transactionRepository.save(transaction);

        CompletableFuture.runAsync(() -> {
            try {
                emailService.sendEmail(
                        senderAccount.getUser().getEmail(),
                        "Transferencia realizada con éxito",
                        String.format("Tu transferencia de $%s a la cuenta %s ha sido procesada con éxito. Tu nuevo saldo es $%s.",
                                requestDto.getAmount(),
                                receiverAccount.getNumber(),
                                senderAccount.getBalance())
                );

                emailService.sendEmail(
                        receiverAccount.getUser().getEmail(),
                        "Has recibido una transferencia",
                        String.format("Has recibido una transferencia de $%s desde la cuenta %s. Tu nuevo saldo es $%s.",
                                requestDto.getAmount(),
                                senderAccount.getNumber(),
                                receiverAccount.getBalance())
                );
            } catch (Exception e) {
                log.error("Error al enviar emails de notificación: {}", e.getMessage(), e);

            }
        });

        return new TransactionResponseDto(
                transaction.getAccountSend().getId(),
                transaction.getAccountReceiving().getId(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getStatus(),
                transaction.getTransactionType()
        );
    }


    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionHistory(Long accountId) {
        {
            String username = this.utilsService.getAuthenticatedUser();
            if (username == null) {
                throw new CustomAuthenticationException("Error: usuario no autenticado");
            }

            AccountBank userAccount = accountBankRepository
                    .findByIdAndAccountStatus(accountId, AccountStatus.ACTIVE)
                    .orElseThrow(() -> new ModelNotFoundException("Cuenta bancaria no encontrada o inactiva"));
            this.utilsService.validateOwnership(userAccount, username);

            List<BankTransaction> transactions = transactionRepository.findAllTransactionsOrderedByMonth(accountId);

            return transactions.stream()
                    .map(transaction -> {
                        boolean isIncoming = transaction.getAccountReceiving().getId().equals(accountId);
                        return new TransactionResponseDto(
                                transaction.getAccountSend().getId(),
                                transaction.getAccountReceiving().getId(),
                                transaction.getAmount(),
                                transaction.getDate(),
                                transaction.getStatus(),
                                isIncoming ? TransactionType.RECEIVING_TRANSACTION : TransactionType.SENDING_TRANSACTION
                        );
                    })
                    .collect(Collectors.toList());
        }
    }

}





