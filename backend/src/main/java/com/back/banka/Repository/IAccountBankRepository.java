package com.back.banka.Repository;

import com.back.banka.Model.AccountBank;
import com.back.banka.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountBankRepository extends JpaRepository<AccountBank, Long> {
    boolean existsByNumber(String number);
    int countByUser(User user);
}
