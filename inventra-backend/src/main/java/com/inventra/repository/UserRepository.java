package com.inventra.repository;

import com.inventra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String token);

    // users waiting for admin approval
    List<User> findByApprovedFalse();

    // locked accounts
    List<User> findByAccountLockedTrue();

}