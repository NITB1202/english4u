package com.nitb.authservice.service;

import com.nitb.authservice.entity.Account;
import com.nitb.authservice.grpc.*;
import org.springframework.data.domain.Page;

public interface AuthService {
    Account loginWithCredentials(LoginWithCredentialsRequest request);
    Account loginWithProvider(LoginWithProviderRequest request);

    boolean IsAccountRegistered(IsAccountRegisteredRequest request);
    boolean validateEmail(String email);
    boolean validatePassword(String password);

    void registerWithCredentials(RegisterWithCredentialsRequest request);
    void registerWithProvider(RegisterWithProviderRequest request);
    void resetPassword(ResetPasswordRequest request);

    Account createAdminAccount(CreateAdminAccountRequest request);
    void updateRole(UpdateRoleRequest request);

    Page<Account> getLearners(GetAccountsRequest request);
    Page<Account> getAdmins(GetAccountsRequest request);
    Page<Account> searchLearnerByEmail(SearchAccountByEmailRequest request);
    Page<Account> searchAdminByEmail(SearchAccountByEmailRequest request);
}
