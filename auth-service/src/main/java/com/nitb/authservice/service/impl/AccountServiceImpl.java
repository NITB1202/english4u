package com.nitb.authservice.service.impl;

import com.nitb.authservice.entity.Account;
import com.nitb.authservice.grpc.*;
import com.nitb.authservice.repository.AccountRepository;
import com.nitb.authservice.service.AccountService;
import com.nitb.common.enums.Provider;
import com.nitb.common.enums.UserRole;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.common.mappers.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final int DEFAULT_SIZE = 10;

    @Override
    public void createAdminAccount(CreateAdminAccountRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Account account = Account.builder()
                .userId(userId)
                .provider(Provider.LOCAL)
                .email(request.getEmail())
                .hashedPassword(hashedPassword)
                .role(UserRole.ADMIN)
                .build();

        accountRepository.save(account);
    }

    @Override
    public Account getAccountById(GetAccountByIdRequest request) {
        UUID id = UUID.fromString(request.getId());
        return accountRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Account not found.")
        );
    }

    @Override
    public void updateRole(UpdateRoleRequest request) {
        UUID id = UUID.fromString(request.getId());
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found.")
        );

        UserRole newRole = UserRoleMapper.toUserRole(request.getRole());

        if(account.getRole().equals(newRole)){
            throw new BusinessException("Updated role is the same with the old role.");
        }

        account.setRole(newRole);
        accountRepository.save(account);
    }

    @Override
    public String getEmailById(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found.")
        );

        return account.getEmail();
    }

    @Override
    public String getRoleById(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found.")
        );

        return account.getRole().toString();
    }


    @Override
    public Page<Account> getLearners(GetAccountsRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return accountRepository.findByRole(UserRole.LEARNER, PageRequest.of(page, size));
    }

    @Override
    public Page<Account> getAdmins(GetAccountsRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return accountRepository.findByRole(UserRole.ADMIN, PageRequest.of(page, size));
    }

    @Override
    public Page<Account> searchLearnerByEmail(SearchAccountByEmailRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return accountRepository.findByEmailContainingIgnoreCaseAndRole(request.getKeyword(), UserRole.LEARNER, PageRequest.of(page, size));
    }

    @Override
    public Page<Account> searchAdminByEmail(SearchAccountByEmailRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return accountRepository.findByEmailContainingIgnoreCaseAndRole(request.getKeyword(), UserRole.ADMIN, PageRequest.of(page, size));
    }
}
