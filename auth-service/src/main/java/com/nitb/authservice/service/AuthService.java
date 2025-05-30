package com.nitb.authservice.service;

import com.nitb.authservice.entity.Account;
import com.nitb.authservice.grpc.*;

public interface AuthService {
    Account loginWithCredentials(LoginWithCredentialsRequest request);
    Account loginWithProvider(LoginWithProviderRequest request);

    boolean IsAccountRegistered(IsAccountRegisteredRequest request);
    boolean validateEmail(String email);
    boolean validatePassword(String password);
    String getPasswordRule();

    void registerWithCredentials(RegisterWithCredentialsRequest request);
    void registerWithProvider(RegisterWithProviderRequest request);
    void resetPassword(ResetPasswordRequest request);
}
