package com.lucasavs.cryptoexchange.repository;

import com.lucasavs.cryptoexchange.entity.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findByUserIdAndAssetSymbol(UUID userId, String assetSymbol);

    <S extends Account> S save(S account);

    List<Account> findByUserId(UUID userId);
}
