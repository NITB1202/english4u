package com.nitb.authservice.service.impl;

import com.nitb.authservice.entity.Account;
import com.nitb.authservice.grpc.*;
import com.nitb.authservice.repository.AccountRepository;
import com.nitb.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;

    @Override
    public Account loginWithCredentials(LoginWithCredentialsRequest request) {
        return null;
    }

    @Override
    public Account loginWithProvider(LoginWithProviderRequest request) {
        return null;
    }

    @Override
    public String generateAccessToken(Account account) {
        return "";
    }

    @Override
    public String generateRefreshToken(Account account) {
        return "";
    }

    @Override
    public boolean validateRegisterInfo(ValidateRegisterInfoRequest request) {
        return false;
    }

    @Override
    public void sendVerificationEmail(SendVerificationEmailRequest request) {

    }

    @Override
    public void register(RegisterRequest request) {

    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

    }

    @Override
    public Account createAdminAccount(CreateAdminAccountRequest request) {
        return null;
    }

    @Override
    public void updateRole(UpdateRoleRequest request) {

    }

    @Override
    public Page<Account> getLearners(GetAccountsRequest request) {
        return null;
    }

    @Override
    public Page<Account> getAdmins(GetAccountsRequest request) {
        return null;
    }

    @Override
    public Page<Account> searchLearnerByEmail(SearchAccountByEmailRequest request) {
        return null;
    }

    @Override
    public Page<Account> searchAdminByEmail(SearchAccountByEmailRequest request) {
        return null;
    }
}
