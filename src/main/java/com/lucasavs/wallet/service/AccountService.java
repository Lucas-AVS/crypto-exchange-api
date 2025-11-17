package com.lucasavs.wallet.service;

import com.lucasavs.wallet.dto.AccountCreateRequest;
import com.lucasavs.wallet.dto.AccountDto;
import com.lucasavs.wallet.dto.AccountUpdateRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountDto> findByUserId(UUID userId);

    AccountDto findByUserIdAndAssetSymbol(UUID userId, String assetSymbol);

    AccountDto create(UUID authenticatedUserId, AccountCreateRequest request);

    AccountDto update(UUID authenticatedUserId, String assetSymbol, AccountUpdateRequest request);

    AccountDto updateBalance(UUID userId, String assetSymbol, BigDecimal amountDelta);
}
