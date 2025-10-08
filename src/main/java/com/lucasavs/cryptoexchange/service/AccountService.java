package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.dto.AccountCreateRequest;
import com.lucasavs.cryptoexchange.dto.AccountDto;
import com.lucasavs.cryptoexchange.dto.AccountUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    List<AccountDto> findByUserId(UUID userId);

    AccountDto findByUserIdAndAssetSymbol(UUID userId, String assetSymbol);

    AccountDto create(UUID authenticatedUserId, AccountCreateRequest request);

    AccountDto update(UUID authenticatedUserId, String assetSymbol, AccountUpdateRequest request);
}
