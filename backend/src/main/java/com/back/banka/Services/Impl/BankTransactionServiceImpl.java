package com.back.banka.Services.Impl;

import com.back.banka.Dtos.RequestDto.TransactionRequestDto;
import com.back.banka.Dtos.ResponseDto.TransactionResponseDto;
import com.back.banka.Enums.AccountStatus;
import com.back.banka.Enums.StatusTransactions;
import com.back.banka.Enums.TransactionType;
import com.back.banka.Exceptions.Custom.BadRequestExceptions;
import com.back.banka.Exceptions.Custom.UserNotFoundException;
import com.back.banka.Model.AccountBank;
import com.back.banka.Model.BankTransaction;
import com.back.banka.Model.User;
import com.back.banka.Repository.BankTransactionRepository;
import com.back.banka.Repository.IAccountBankRepository;
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


    @Transactional
    public TransactionResponseDto transfer(TransactionRequestDto requestDto) {
        try {
            User authenticateUser = userService.getAuthenticatedUser();

            //Obtener la cuenta del usuario autenticado
            AccountBank senderAccount = accountBankRepository.findByUserIdAndAccountStatus(authenticateUser.getId(), AccountStatus.ACTIVE)
                    .orElseThrow(() -> new UserNotFoundException("Cuenta bancaria no encontrada o inactiva para el usuario autenticado"));

            //Obtener la cuenta de destino

            AccountBank receiverAccount = accountBankRepository.findByNumber(requestDto.getReceiverAccountNumber())
                    .orElseThrow(() -> new UserNotFoundException("Cuenta de destino no encontrada"));

            //Validaciones de transferencia
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
            if (!authenticateUser.isStatus()) {
                throw new BadRequestExceptions("El usuario no está autorizado para realizar transacciones.");
            }
            //Realizar la transferencia
            senderAccount.setBalance(senderAccount.getBalance().subtract(requestDto.getAmount()));
            receiverAccount.setBalance(receiverAccount.getBalance().add(requestDto.getAmount()));

            accountBankRepository.save(senderAccount);
            accountBankRepository.save(receiverAccount);

            //Registrar la transacción

            BankTransaction transaction = BankTransaction.builder()
                    .accountSend(senderAccount)
                    .accountReceiving(receiverAccount)
                    .amount(requestDto.getAmount())
                    .transactionType(TransactionType.SENDING_TRANSACTION)
                    .date(LocalDateTime.now())
                    .status(StatusTransactions.COMPLETED)
                    .build();

            transactionRepository.save(transaction);

            //Notificación por correo electrónico
            emailService.sendEmail(authenticateUser.getEmail(),
                    "Transferencia realizada con éxito",
                    "Tu transferencia de $ " + requestDto.getAmount() +
                            " a la cuenta " + receiverAccount.getNumber() + " ha sido procesada con éxito. ");

            emailService.sendEmail(receiverAccount.getUser().getEmail(),
                    "Has recibido una transferencia",
                    "Has recibido una transferencia de $" + requestDto.getAmount() +
                            " desde la cuenta " + senderAccount.getNumber() + ".");

            return new TransactionResponseDto(
                    transaction.getAccountSend().getUser().getId(),
                    transaction.getAccountReceiving().getUser().getId(),
                    transaction.getAmount(),
                    transaction.getDate(),
                    transaction.getStatus()
            );

        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al realizar la transferencia: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionHistory() {
        try {

            User authenticatedUser = userService.getAuthenticatedUser();

            //Buscar la cuenta bancaria del usuario autenticado
            AccountBank userAccount = accountBankRepository
                    .findByUserIdAndAccountStatus(authenticatedUser.getId(), AccountStatus.ACTIVE)
                    .orElseThrow(() -> new UserNotFoundException("Cuenta bancaria no encontrada o inactiva"));

            //Obtener todas las transacciones donde el usuario
            List<BankTransaction> transactions = transactionRepository.findTransactionsByAccount(userAccount);

            return transactions.stream()
                    .map(transaction -> new TransactionResponseDto(
                            transaction.getAccountSend().getUser().getId(),
                            transaction.getAccountReceiving().getUser().getId(),
                            transaction.getAmount(),
                            transaction.getDate(),
                            transaction.getStatus()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e){
            throw new RuntimeException("Error al obtener el historial de transacciones: " + e.getMessage());


        }
    }
}





