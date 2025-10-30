package com.lucasavs.wallet.repository;

import com.lucasavs.wallet.entity.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findByUserIdAndAssetSymbol(UUID userId, String assetSymbol);

    <S extends Account> S save(S account);

    List<Account> findByUserId(UUID userId);
}
