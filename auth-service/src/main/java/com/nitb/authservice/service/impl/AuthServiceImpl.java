package com.nitb.authservice.service.impl;

import com.nitb.authservice.entity.Account;
import com.nitb.authservice.grpc.*;
import com.nitb.authservice.repository.AccountRepository;
import com.nitb.authservice.service.AuthService;
import com.nitb.authservice.service.CodeService;
import com.nitb.common.enums.Provider;
import com.nitb.common.enums.UserRole;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.common.mappers.ProviderMapper;
import com.nitb.common.mappers.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeService codeService;
    private final int DEFAULT_SIZE = 10;

    @Override
    public Account loginWithCredentials(LoginWithCredentialsRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail());

        if(account == null) {
            throw new NotFoundException("Invalid email.");
        }

        if(!passwordEncoder.matches(request.getPassword(), account.getHashedPassword())) {
            throw new BusinessException("Incorrect password.");
        }

        return account;
    }

    @Override
    public Account loginWithProvider(LoginWithProviderRequest request) {
        String providerId = request.getProviderId();
        Provider provider = ProviderMapper.toProvider(request.getProvider());

        Account account = accountRepository.findByProviderIdAndProvider(providerId, provider);

        if(account == null) {
            throw new NotFoundException("Incorrect provider id or provider.");
        }

        return account;
    }


    @Override
    public boolean IsAccountRegistered(IsAccountRegisteredRequest request) {
        String accountId = request.getProviderId();
        Provider provider = ProviderMapper.toProvider(request.getProvider());

        return accountRepository.existsByProviderIdAndProvider(accountId, provider);
    }

    @Override
    public boolean validateEmail(String email) {
        return !accountRepository.existsByEmail(email);
    }

    @Override
    public boolean validatePassword(String password) {
        //Must be at least 3 characters long and contain both letters and numbers.
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,}$";
        return password.matches(passwordRegex);
    }

    @Override
    public String getPasswordRule() {
        return "Password must be at least 3 characters long and contain both letters and numbers.";
    }


    @Override
    public void registerWithCredentials(RegisterWithCredentialsRequest request) {
        if(codeService.verifyCode(request.getEmail(), request.getVerificationCode(), VerificationType.REGISTER)){
            if(!validateEmail(request.getEmail())) {
                throw new BusinessException("This email has been used.");
            }

            if(!validatePassword(request.getPassword())) {
                throw new BusinessException(getPasswordRule());
            }

            UUID userId = UUID.fromString(request.getUserId());
            String hashedPassword = passwordEncoder.encode(request.getPassword());

            Account account = Account.builder()
                    .userId(userId)
                    .provider(Provider.LOCAL)
                    .email(request.getEmail())
                    .hashedPassword(hashedPassword)
                    .role(UserRole.LEARNER)
                    .build();

            accountRepository.save(account);
        }
        else{
            throw new BusinessException("Verify failed.");
        }
    }

    @Override
    public void registerWithProvider(RegisterWithProviderRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        Provider provider = ProviderMapper.toProvider(request.getProvider());

        Account account = Account.builder()
                .userId(userId)
                .provider(provider)
                .providerId(request.getProviderId())
                .email(request.getEmail())
                .role(UserRole.LEARNER)
                .build();

        accountRepository.save(account);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if(codeService.verifyCode(request.getEmail(), request.getVerificationCode(), VerificationType.RESET_PASSWORD)){
            Account account = accountRepository.findByEmail(request.getEmail());

            if(account == null) {
                throw new NotFoundException("Account not found.");
            }

            if(!validatePassword(request.getNewPassword())) {
                throw new BusinessException(getPasswordRule());
            }

            String hashedPassword = passwordEncoder.encode(request.getNewPassword());
            account.setHashedPassword(hashedPassword);
            accountRepository.save(account);
        }
        else{
            throw new BusinessException("Reset password failed.");
        }
    }


    @Override
    public void createAdminAccount(CreateAdminAccountRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        if(!validateEmail(request.getEmail())) {
            throw new BusinessException("This email has been used.");
        }

        if(!validatePassword(request.getPassword())) {
            throw new BusinessException(getPasswordRule());
        }

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
