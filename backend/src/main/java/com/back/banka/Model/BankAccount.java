package com.back.banka.Model;

import com.back.banka.Enums.AccountStatus;
import com.back.banka.Enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(nullable = false)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "accountSend",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<BankTransaction> transactions;

    @OneToMany(mappedBy = "accountReceiving",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<BankTransaction> receivingTransactions;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

}
