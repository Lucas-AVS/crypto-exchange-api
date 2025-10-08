package com.lucasavs.cryptoexchange.controller;

import com.lucasavs.cryptoexchange.dto.AccountCreateRequest;
import com.lucasavs.cryptoexchange.dto.AccountDto;
import com.lucasavs.cryptoexchange.dto.AccountUpdateRequest;
import com.lucasavs.cryptoexchange.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/me/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AccountCreateRequest request) {

        UUID userId = UUID.fromString(userDetails.getUsername());
        return accountService.create(userId, request);
    }

    @PatchMapping("/{assetSymbol}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto patchUpdateAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String assetSymbol,
            @Valid @RequestBody AccountUpdateRequest request) {

        UUID userId = UUID.fromString(userDetails.getUsername());
        return accountService.update(userId, assetSymbol, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getMyAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        UUID userId = UUID.fromString(userDetails.getUsername());
        return accountService.findByUserId(userId);
    }

    @GetMapping("/{assetSymbol}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getMyAccountBySymbol(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String assetSymbol) {

        UUID userId = UUID.fromString(userDetails.getUsername());
        return accountService.findByUserIdAndAssetSymbol(userId, assetSymbol);
    }
}