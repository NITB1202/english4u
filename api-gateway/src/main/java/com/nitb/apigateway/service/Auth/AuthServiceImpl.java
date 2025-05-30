package com.nitb.apigateway.service.Auth;

import com.nitb.apigateway.dto.Auth.Auth.request.*;
import com.nitb.apigateway.dto.Auth.Auth.response.GenerateAccessTokenResponseDto;
import com.nitb.apigateway.dto.Auth.Auth.response.LoginResponseDto;
import com.nitb.apigateway.dto.Auth.Auth.response.OAuthUserInfo;
import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.grpc.AuthServiceGrpcClient;
import com.nitb.apigateway.grpc.UserServiceGrpcClient;
import com.nitb.apigateway.mapper.ActionMapper;
import com.nitb.apigateway.mapper.AuthMapper;
import com.nitb.authservice.grpc.AccountSummaryResponse;
import com.nitb.authservice.grpc.LoginResponse;
import com.nitb.authservice.grpc.VerificationType;
import com.nitb.common.enums.Provider;
import com.nitb.common.enums.UserRole;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.common.mappers.UserRoleMapper;
import com.nitb.common.utils.JwtUtils;
import com.nitb.userservice.grpc.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthServiceGrpcClient authGrpc;
    private final UserServiceGrpcClient userGrpc;
    private final WebClient webClient;

    @Override
    public Mono<LoginResponseDto> loginWithCredentials(LoginWithCredentialsRequestDto request) {
        return Mono.fromCallable(()->{
            LoginResponse response = authGrpc.loginWithCredentials(request);
            return AuthMapper.toLoginResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<LoginResponseDto> loginWithProvider(LoginWithProviderRequestDto request) {
        if(request.getProvider() == Provider.LOCAL) {
            throw new BusinessException("With provider LOCAL, please use the standard login.");
        }

        Mono<OAuthUserInfo> userInfoMono = null;

        switch (request.getProvider()) {
            case GOOGLE -> userInfoMono = getUserInfoWithGoogle(request.getAccessToken());
            case FACEBOOK -> userInfoMono = getUserInfoWithFacebook(request.getAccessToken());
        }

        return userInfoMono.flatMap(userInfo ->{
            boolean isRegistered = authGrpc.isAccountRegistered(userInfo.getId()).getIsRegistered();

            if(!isRegistered) {
                CreateUserResponse user = userGrpc.createUser(userInfo.getName(), userInfo.getPicture());
                UUID userId = UUID.fromString(user.getUserId());
                authGrpc.registerWithProvider(userInfo.getId(), request.getProvider(), userId, userInfo.getEmail());
            }

            LoginResponse response = authGrpc.loginWithProvider(userInfo.getId(), request.getProvider());
            return Mono.just(AuthMapper.toLoginResponseDto(response));
        });
    }

    private Mono<Map<String, Object>> sendRequest(String accessToken, String url) {
        return webClient
                .get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public Mono<OAuthUserInfo> getUserInfoWithGoogle(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v3/userinfo";
        return sendRequest(accessToken, url).map(response -> {
            return OAuthUserInfo.builder()
                    .id((String) response.get("sub"))
                    .name((String) response.get("name"))
                    .email((String) response.get("email"))
                    .picture((String) response.get("picture"))
                    .build();
        });
    }

    public Mono<OAuthUserInfo> getUserInfoWithFacebook(String accessToken) {
        String url = "https://graph.facebook.com/me?fields=id,name,email,picture";
        return sendRequest(accessToken, url).map(response -> {
            String pictureUrl = null;
            if (response.containsKey("picture")) {
                Map pictureMap = (Map) response.get("picture");
                Map dataMap = (Map) pictureMap.get("data");
                pictureUrl = (String) dataMap.get("url");
            }

            return OAuthUserInfo.builder()
                    .id((String) response.get("id"))
                    .name((String) response.get("name"))
                    .email((String) response.get("email"))
                    .picture(pictureUrl)
                    .build();
        });
    }


    @Override
    public Mono<ActionResponseDto> validateRegisterInfo(ValidateRegisterInfoRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = authGrpc.validateRegisterInfo(request);

            if(response.getSuccess()) {
                authGrpc.sendVerificationEmail(request.getEmail(), VerificationType.REGISTER);
            }

            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> registerWithCredentials(RegisterWithCredentialsRequestDto request) {
        return Mono.fromCallable(()->{
            CreateUserResponse user = userGrpc.createUser(request.getName(), null);
            UUID userId = UUID.fromString(user.getUserId());
            ActionResponse response = authGrpc.registerWithCredentials(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> sendResetPasswordCode(String email) {
        return Mono.fromCallable(()->{
            ActionResponse response = authGrpc.sendVerificationEmail(email, VerificationType.RESET_PASSWORD);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> resetPassword(ResetPasswordRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = authGrpc.resetPassword(request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<GenerateAccessTokenResponseDto> generateAccessToken(String refreshToken) {
        return Mono.fromCallable(()->{
            if(!JwtUtils.isTokenValid(refreshToken)) {
                throw new BusinessException("Token is expired.");
            }

            UUID accountId = JwtUtils.extractId(refreshToken);
            AccountSummaryResponse account = authGrpc.getAccountById(accountId);

            UUID userId = UUID.fromString(account.getUserId());
            UserRole role = UserRoleMapper.fromString(account.getRole());

            String accessToken = JwtUtils.generateAccessToken(userId, role);
            return AuthMapper.toGenerateAccessTokenResponseDto(accessToken);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
