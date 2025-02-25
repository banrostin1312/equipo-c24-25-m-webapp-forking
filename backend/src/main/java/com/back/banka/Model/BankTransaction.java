package com.back.banka.Model;

import com.back.banka.Enums.StatusTransactions;
import com.back.banka.Enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_transaction")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_id")
    private BankAccount accountSend;

   @JoinColumn(name = "receiving_id")
   @ManyToOne(fetch = FetchType.LAZY)
   private BankAccount accountReceiving;

   @Column(nullable = false)
   private BigDecimal amount;

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   private StatusTransactions status;
}
