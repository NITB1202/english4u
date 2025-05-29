package com.nitb.authservice.repository;

import com.nitb.authservice.entity.Account;
import com.nitb.common.enums.Provider;
import com.nitb.common.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByProviderIdAndProvider(String providerId, Provider provider);
    boolean existsByEmail(String email);
    Account findByEmail(String email);
    Account findByProviderIdAndProvider(String providerId, Provider provider);
    Page<Account> findByRole(UserRole role, Pageable pageable);
    Page<Account> findByEmailContainingIgnoreCaseAndRole(String email, UserRole role, Pageable pageable);
}