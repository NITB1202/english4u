package com.nitb.authservice.controller;

import com.nitb.authservice.grpc.*;
import com.nitb.authservice.service.AuthService;
import com.nitb.common.grpc.ActionResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AuthController extends AuthServiceGrpc.AuthServiceImplBase {
    private final AuthService authService;

    @Override
    public void loginWithCredentials(LoginWithCredentialsRequest request, StreamObserver<LoginResponse> responseObserver) {

    }

    @Override
    public void loginWithProvider(LoginWithProviderRequest request, StreamObserver<LoginResponse> responseObserver) {

    }

    @Override
    public void validateRegisterInfo(ValidateRegisterInfoRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void sendVerificationEmail(SendVerificationEmailRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void register(RegisterRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void resetPassword(ResetPasswordRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void createAdminAccount(CreateAdminAccountRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void updateRole(UpdateRoleRequest request, StreamObserver<ActionResponse> responseObserver) {

    }

    @Override
    public void getLearners(GetAccountsRequest request, StreamObserver<AccountsResponse> responseObserver) {

    }

    @Override
    public void getAdmins(GetAccountsRequest request, StreamObserver<AccountsResponse> responseObserver) {

    }

    @Override
    public void searchLearnerByEmail(SearchAccountByEmailRequest request, StreamObserver<AccountsResponse> responseObserver) {

    }

    @Override
    public void searchAdminByEmail(SearchAccountByEmailRequest request, StreamObserver<AccountsResponse> responseObserver) {

    }
}
