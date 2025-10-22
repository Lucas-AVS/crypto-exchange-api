package com.lucasavs.wallet.controller;

import com.lucasavs.wallet.dto.AccountCreateRequest;
import com.lucasavs.wallet.dto.AccountDto;
import com.lucasavs.wallet.dto.AccountUpdateRequest;
import com.lucasavs.wallet.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wallet/me/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody AccountCreateRequest request) {

        return accountService.create(UUID.fromString(userId), request);
    }

    @PatchMapping("/{assetSymbol}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto patchUpdateAccount(
            @AuthenticationPrincipal String userId,
            @PathVariable String assetSymbol,
            @Valid @RequestBody AccountUpdateRequest request) {

        return accountService.update(UUID.fromString(userId), assetSymbol, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getMyAccounts(@AuthenticationPrincipal String userId) {
        return accountService.findByUserId(UUID.fromString(userId));
    }

    @GetMapping("/{assetSymbol}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getMyAccountBySymbol(
            @AuthenticationPrincipal String userId,
            @PathVariable String assetSymbol) {

        return accountService.findByUserIdAndAssetSymbol(UUID.fromString(userId), assetSymbol);
    }
}