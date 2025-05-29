package com.nitb.authservice.controller;

import com.nitb.authservice.entity.Account;
import com.nitb.authservice.grpc.*;
import com.nitb.authservice.mapper.AccountMapper;
import com.nitb.authservice.mapper.AuthMapper;
import com.nitb.authservice.service.AuthService;
import com.nitb.authservice.service.CodeService;
import com.nitb.authservice.service.MailService;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.common.utils.JwtUtils;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

@GrpcService
@RequiredArgsConstructor
public class AuthController extends AuthServiceGrpc.AuthServiceImplBase {
    private final AuthService authService;
    private final CodeService codeService;
    private final MailService mailService;

    @Override
    public void loginWithCredentials(LoginWithCredentialsRequest request, StreamObserver<LoginResponse> responseObserver) {
        Account account = authService.loginWithCredentials(request);
        String accessToken = JwtUtils.generateAccessToken(account.getUserId(), account.getRole());
        String refreshToken = JwtUtils.generateRefreshToken(account.getUserId());
        LoginResponse loginResponse = AuthMapper.toLoginResponse(accessToken, refreshToken);
        responseObserver.onNext(loginResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void loginWithProvider(LoginWithProviderRequest request, StreamObserver<LoginResponse> responseObserver) {
        Account account = authService.loginWithProvider(request);
        String accessToken = JwtUtils.generateAccessToken(account.getUserId(), account.getRole());
        String refreshToken = JwtUtils.generateRefreshToken(account.getUserId());
        LoginResponse loginResponse = AuthMapper.toLoginResponse(accessToken, refreshToken);
        responseObserver.onNext(loginResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void isAccountRegistered(IsAccountRegisteredRequest request, StreamObserver<IsAccountRegisteredResponse> responseObserver) {
        boolean isRegistered = authService.IsAccountRegistered(request);
        IsAccountRegisteredResponse response = AuthMapper.toIsAccountRegisteredResponse(isRegistered);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void validateRegisterInfo(ValidateRegisterInfoRequest request, StreamObserver<ActionResponse> responseObserver) {
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Valid register information.")
                .build();

        if(!authService.validateEmail(request.getEmail())) {
            response = ActionResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Email is already used.")
                    .build();
        }

        if(!authService.validatePassword(request.getPassword())) {
            response = ActionResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(authService.getPasswordRule())
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendVerificationEmail(SendVerificationEmailRequest request, StreamObserver<ActionResponse> responseObserver) {
        String code = codeService.generateCode(request.getEmail(), request.getType());
        mailService.sendVerificationEmail(code, request.getEmail());

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Verification email has been sent.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void registerWithCredentials(RegisterWithCredentialsRequest request, StreamObserver<ActionResponse> responseObserver) {
        authService.registerWithCredentials(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Register successful.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void registerWithProvider(RegisterWithProviderRequest request, StreamObserver<ActionResponse> responseObserver) {
        authService.registerWithProvider(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Register successful.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void resetPassword(ResetPasswordRequest request, StreamObserver<ActionResponse> responseObserver) {
        authService.resetPassword(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Reset password successful.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createAdminAccount(CreateAdminAccountRequest request, StreamObserver<ActionResponse> responseObserver) {
        authService.createAdminAccount(request);
        mailService.sendAdminAccountEmail(request.getEmail(), request.getPassword());

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Account info has been sent to the registered email.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateRole(UpdateRoleRequest request, StreamObserver<ActionResponse> responseObserver) {
        String email = authService.updateRole(request);
        mailService.sendUpdateRoleEmail(email, request.getRole());

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update role successful. Notification email has been sent to the user.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getLearners(GetAccountsRequest request, StreamObserver<AccountsResponse> responseObserver) {
        Page<Account> accounts = authService.getLearners(request);
        AccountsResponse response = AccountMapper.toAccountsResponse(accounts);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAdmins(GetAccountsRequest request, StreamObserver<AccountsResponse> responseObserver) {
        Page<Account> accounts = authService.getAdmins(request);
        AccountsResponse response = AccountMapper.toAccountsResponse(accounts);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchLearnerByEmail(SearchAccountByEmailRequest request, StreamObserver<AccountsResponse> responseObserver) {
        Page<Account> accounts = authService.searchLearnerByEmail(request);
        AccountsResponse response = AccountMapper.toAccountsResponse(accounts);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchAdminByEmail(SearchAccountByEmailRequest request, StreamObserver<AccountsResponse> responseObserver) {
        Page<Account> accounts = authService.searchAdminByEmail(request);
        AccountsResponse response = AccountMapper.toAccountsResponse(accounts);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
