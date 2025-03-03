package com.back.banka.Services.Impl;

import com.back.banka.Dtos.RequestDto.TransactionRequestDto;
import com.back.banka.Dtos.ResponseDto.TransactionResponseDto;
import com.back.banka.Enums.AccountStatus;
import com.back.banka.Enums.StatusTransactions;
import com.back.banka.Enums.TransactionType;
import com.back.banka.Exceptions.Custom.BadRequestExceptions;
import com.back.banka.Exceptions.Custom.ModelNotFoundException;
import com.back.banka.Exceptions.Custom.UserNotFoundException;
import com.back.banka.Model.AccountBank;
import com.back.banka.Model.BankTransaction;
import com.back.banka.Repository.BankTransactionRepository;
import com.back.banka.Repository.IAccountBankRepository;
import com.back.banka.Utils.IUtilsService;
import org.springframework.transaction.annotation.Transactional;
import com.back.banka.Services.IServices.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BankTransactionServiceImpl {
    private final BankTransactionRepository transactionRepository;
    private final IAccountBankRepository accountBankRepository;
    private final IUserService userService; //Para obtener el usuario autenticado
    private final EmailServiceImpl emailService;
    private final IUtilsService utilsService;


    @Transactional
    public TransactionResponseDto transfer(Long senderAccountId, TransactionRequestDto requestDto) {
        try {
            AccountBank senderAccount = accountBankRepository.findByIdAndAccountStatus(senderAccountId, AccountStatus.ACTIVE)
                    .orElseThrow(() -> new UserNotFoundException("Cuenta bancaria remitente no encontrada o inactiva"));

            AccountBank receiverAccount = accountBankRepository.findByNumber(requestDto.getReceiverAccountNumber())
                    .orElseThrow(() -> new UserNotFoundException("Cuenta de destino no encontrada"));

            if (requestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestExceptions("El monto a transferir debe ser mayor a cero.");
            }
            if (senderAccount.getBalance().compareTo(requestDto.getAmount()) < 0) {
                throw new BadRequestExceptions("Saldo insuficiente para realizar la transferencia.");
            }
            if (!senderAccount.getAccountStatus().equals(AccountStatus.ACTIVE) ||
                    !receiverAccount.getAccountStatus().equals(AccountStatus.ACTIVE)) {
                throw new BadRequestExceptions("Ambas cuentas deben estar activas.");
            }

            senderAccount.setBalance(senderAccount.getBalance().subtract(requestDto.getAmount()));
            receiverAccount.setBalance(receiverAccount.getBalance().add(requestDto.getAmount()));

            accountBankRepository.save(senderAccount);
            accountBankRepository.save(receiverAccount);

            BankTransaction transaction = BankTransaction.builder()
                    .accountSend(senderAccount)
                    .accountReceiving(receiverAccount)
                    .amount(requestDto.getAmount())
                    .transactionType(TransactionType.SENDING_TRANSACTION)
                    .date(LocalDateTime.now())
                    .status(StatusTransactions.COMPLETED)
                    .build();

            transactionRepository.save(transaction);

            emailService.sendEmail(senderAccount.getUser().getEmail(),
                    "Transferencia realizada con éxito",
                    "Tu transferencia de $ " + requestDto.getAmount() +
                            " a la cuenta " + receiverAccount.getNumber() + " ha sido procesada con éxito.");

            emailService.sendEmail(receiverAccount.getUser().getEmail(),
                    "Has recibido una transferencia",
                    "Has recibido una transferencia de $" + requestDto.getAmount() +
                            " desde la cuenta " + senderAccount.getNumber() + ".");

            return new TransactionResponseDto(
                    transaction.getAccountSend().getId(),
                    transaction.getAccountReceiving().getId(),
                    transaction.getAmount(),
                    transaction.getDate(),
                    transaction.getStatus()
            );

        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al realizar la transferencia: " + e.getMessage());
        }
    }


    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionHistory(Long accountId) {
         {
            AccountBank userAccount = accountBankRepository
                    .findByIdAndAccountStatus(accountId, AccountStatus.ACTIVE)
                    .orElseThrow(() -> new ModelNotFoundException("Cuenta bancaria no encontrada o inactiva"));

            List<BankTransaction> transactions = transactionRepository.findAllTransactionsOrderedByMonth(accountId);

            return transactions.stream()
                    .map(transaction -> new TransactionResponseDto(
                            transaction.getAccountSend().getId(),
                            transaction.getAccountReceiving().getId(),
                            transaction.getAmount(),
                            transaction.getDate(),
                            transaction.getStatus()
                    ))
                    .collect(Collectors.toList());

        }
    }

}





