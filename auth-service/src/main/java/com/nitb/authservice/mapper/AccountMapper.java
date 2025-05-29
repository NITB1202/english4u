package com.nitb.authservice.mapper;

import com.nitb.authservice.entity.Account;
import com.nitb.authservice.grpc.AccountResponse;
import com.nitb.authservice.grpc.AccountsResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class AccountMapper {
    private AccountMapper() {}

    public static AccountResponse toAccountResponse(Account account) {
        String email = account.getEmail() != null ? account.getEmail() : "";

        return AccountResponse.newBuilder()
                .setId(account.getId().toString())
                .setUserId(account.getUserId().toString())
                .setEmail(email)
                .build();
    }

    public static AccountsResponse toAccountsResponse(Page<Account> accounts) {
        List<AccountResponse> responses = accounts.getContent().stream()
                .map(AccountMapper::toAccountResponse)
                .toList();

        return AccountsResponse.newBuilder()
                .addAllAccounts(responses)
                .setTotalItems(accounts.getTotalElements())
                .setTotalPages(accounts.getTotalPages())
                .build();
    }
}
