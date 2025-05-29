package com.nitb.authservice.repository;

import com.nitb.authservice.entity.Account;
import com.nitb.common.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByProviderIdAndProvider(String providerId, Provider provider);
    boolean existsByEmail(String email);
    Account findByEmail(String email);
    Account findByProviderIdAndProvider(String providerId, Provider provider);
}