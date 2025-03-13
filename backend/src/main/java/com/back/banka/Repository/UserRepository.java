package com.back.banka.Repository;

import java.util.Optional;
import com.back.banka.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByDNIAndIdNot(String DNI, Long id);

    boolean existsByDNI(String dni);
    Optional<User> findByDNI(String dni);
    Optional<User> findById(Long id);

}