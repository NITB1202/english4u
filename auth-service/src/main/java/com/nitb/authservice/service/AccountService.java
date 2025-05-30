package com.nitb.authservice.service;

import com.nitb.authservice.entity.Account;
import com.nitb.authservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AccountService {
    void createAdminAccount(CreateAdminAccountRequest request);
    Account getAccountById(GetAccountByIdRequest request);
    void updateRole(UpdateRoleRequest request);

    String getEmailById(UUID id);
    String getRoleById(UUID id);

    Page<Account> getLearners(GetAccountsRequest request);
    Page<Account> getAdmins(GetAccountsRequest request);
    Page<Account> searchLearnerByEmail(SearchAccountByEmailRequest request);
    Page<Account> searchAdminByEmail(SearchAccountByEmailRequest request);
}
