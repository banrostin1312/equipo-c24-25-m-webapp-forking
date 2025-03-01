package com.back.banka.Repository;

import com.back.banka.Enums.TokenType;
import com.back.banka.Model.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ITokenRepository extends JpaRepository<Tokens, Long> {
    List<Tokens> findByUserId(Long userId);
    Optional<Tokens> findByTokenType(TokenType tokenType);
    List<Tokens> findAllIExpiredIsFalseOrRevokedIsFalseByUserId(Long userId);
    @Query("SELECT t FROM Tokens t WHERE t.user.id = :userId AND t.expired = false AND t.revoked = false")
    List<Tokens> findAllValidTokensByUserId(Long userId);
    boolean existsByTokenAndUserIdAndExpiredFalseAndRevokedFalse(String token, Long userId);

}
