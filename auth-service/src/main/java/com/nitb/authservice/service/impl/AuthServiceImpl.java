package com.nitb.authservice.service.impl;

import com.nitb.authservice.entity.Account;
import com.nitb.authservice.grpc.*;
import com.nitb.authservice.repository.AccountRepository;
import com.nitb.authservice.service.AuthService;
import com.nitb.common.enums.Provider;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.common.mappers.ProviderMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private final String keyStr = "SECRET_KEY";
    private final Key secretKey = Keys.hmacShaKeyFor(keyStr.getBytes());
    private final long accessTokenExpirationMs = 60 * 60 * 1000; //1h
    private final long refreshTokenExpirationMs = 7 * 24 * 60 * 60 * 1000L; //1w

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
    public String generateAccessToken(Account account) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .setSubject(account.getUserId().toString())
                .claim("role", account.getRole())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(Account account) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);

        return Jwts.builder()
                .setSubject(account.getUserId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
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
    public void sendVerificationEmail(SendVerificationEmailRequest request) {

    }

    @Override
    public void registerWithCredentials(RegisterWithCredentialsRequest request) {

    }

    @Override
    public void registerWithProvider(RegisterWithProviderRequest request) {

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
