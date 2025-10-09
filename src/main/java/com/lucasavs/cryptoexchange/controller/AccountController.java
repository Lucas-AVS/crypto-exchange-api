package com.lucasavs.cryptoexchange.controller;

import com.lucasavs.cryptoexchange.dto.AccountCreateRequest;
import com.lucasavs.cryptoexchange.dto.AccountDto;
import com.lucasavs.cryptoexchange.dto.AccountUpdateRequest;
import com.lucasavs.cryptoexchange.security.CustomUserDetails;
import com.lucasavs.cryptoexchange.security.SecurityConfiguration;
import com.lucasavs.cryptoexchange.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @Operation(security = @SecurityRequirement(name = SecurityConfiguration.SECURITY))
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AccountCreateRequest request) {

        UUID userId = userDetails.getId();
        return accountService.create(userId, request);
    }

    @PatchMapping("/{assetSymbol}")
    @Operation(security = @SecurityRequirement(name = SecurityConfiguration.SECURITY))
    @ResponseStatus(HttpStatus.OK)
    public AccountDto patchUpdateAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String assetSymbol,
            @Valid @RequestBody AccountUpdateRequest request) {

        UUID userId = userDetails.getId();
        return accountService.update(userId, assetSymbol, request);
    }

    @GetMapping
    @Operation(security = @SecurityRequirement(name = SecurityConfiguration.SECURITY))
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getMyAccounts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getId();
        return accountService.findByUserId(userId);
    }

    @GetMapping("/{assetSymbol}")
    @Operation(security = @SecurityRequirement(name = SecurityConfiguration.SECURITY))
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getMyAccountBySymbol(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String assetSymbol) {

        UUID userId = userDetails.getId();
        return accountService.findByUserIdAndAssetSymbol(userId, assetSymbol);
    }
}