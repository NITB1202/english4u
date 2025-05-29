package com.nitb.apigateway.grpc;

import com.nitb.apigateway.dto.Auth.Account.request.CreateAdminAccountRequestDto;
import com.nitb.apigateway.dto.Auth.Auth.request.*;
import com.nitb.authservice.grpc.*;
import com.nitb.common.enums.Provider;
import com.nitb.common.enums.UserRole;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.common.mappers.ProviderMapper;
import com.nitb.common.mappers.UserRoleMapper;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceGrpcClient {
    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

    public LoginResponse loginWithCredentials(LoginWithCredentialsRequestDto dto) {
        LoginWithCredentialsRequest request = LoginWithCredentialsRequest.newBuilder()
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .build();

        return blockingStub.loginWithCredentials(request);
    }

    public LoginResponse loginWithProvider(String providerId, Provider provider) {
        SupportedProvider supportedProvider = ProviderMapper.toSupportedProvider(provider);

        LoginWithProviderRequest request = LoginWithProviderRequest.newBuilder()
                .setProviderId(providerId)
                .setProvider(supportedProvider)
                .build();

        return blockingStub.loginWithProvider(request);
    }

    public IsAccountRegisteredResponse isAccountRegistered(String providerId) {
        IsAccountRegisteredRequest request = IsAccountRegisteredRequest.newBuilder()
                .setProviderId(providerId)
                .build();

        return blockingStub.isAccountRegistered(request);
    }

    public ActionResponse validateRegisterInfo(ValidateRegisterInfoRequestDto dto) {
        ValidateRegisterInfoRequest request = ValidateRegisterInfoRequest.newBuilder()
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .build();

        return blockingStub.validateRegisterInfo(request);
    }

    public ActionResponse sendVerificationEmail(String email, VerificationType type) {
        SendVerificationEmailRequest request = SendVerificationEmailRequest.newBuilder()
                .setEmail(email)
                .setType(type)
                .build();

        return blockingStub.sendVerificationEmail(request);
    }

    public ActionResponse registerWithCredentials(UUID userId, RegisterWithCredentialsRequestDto dto) {
        RegisterWithCredentialsRequest request = RegisterWithCredentialsRequest.newBuilder()
                .setUserId(userId.toString())
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .setVerificationCode(dto.getVerificationCode())
                .build();

        return blockingStub.registerWithCredentials(request);
    }

    public ActionResponse registerWithProvider(String providerId, Provider provider, UUID userId, String email) {
        SupportedProvider supportedProvider = ProviderMapper.toSupportedProvider(provider);

        RegisterWithProviderRequest request = RegisterWithProviderRequest.newBuilder()
                .setProviderId(providerId)
                .setProvider(supportedProvider)
                .setUserId(userId.toString())
                .setEmail(email)
                .build();

        return blockingStub.registerWithProvider(request);
    }

    public ActionResponse resetPassword(ResetPasswordRequestDto dto) {
        ResetPasswordRequest request = ResetPasswordRequest.newBuilder()
                .setVerificationCode(dto.getVerificationCode())
                .setEmail(dto.getEmail())
                .setNewPassword(dto.getNewPassword())
                .build();

        return blockingStub.resetPassword(request);
    }

    public ActionResponse createAdminAccount(UUID userId, CreateAdminAccountRequestDto dto) {
        CreateAdminAccountRequest request = CreateAdminAccountRequest.newBuilder()
                .setUserId(userId.toString())
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .build();

        return blockingStub.createAdminAccount(request);
    }

    public ActionResponse updateRole(UUID id, UserRole role) {
        SupportedRole supportedRole = UserRoleMapper.toSupportedRole(role);

        UpdateRoleRequest request = UpdateRoleRequest.newBuilder()
                .setId(id.toString())
                .setRole(supportedRole)
                .build();

        return blockingStub.updateRole(request);
    }

    public AccountsResponse getLearners(int page, int size) {
        GetAccountsRequest request = GetAccountsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.getLearners(request);
    }

    public AccountsResponse getAdmins(int page, int size) {
        GetAccountsRequest request = GetAccountsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.getAdmins(request);
    }

    public AccountsResponse searchLearnerByEmail(String keyword, int page, int size) {
        SearchAccountByEmailRequest request = SearchAccountByEmailRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.searchLearnerByEmail(request);
    }

    public AccountsResponse searchAdminByEmail(String keyword, int page, int size) {
        SearchAccountByEmailRequest request = SearchAccountByEmailRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.searchAdminByEmail(request);
    }
}
