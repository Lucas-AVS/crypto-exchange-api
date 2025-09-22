package com.lucasavs.cryptoexchange.repository;

import com.lucasavs.cryptoexchange.entity.Asset;

import java.util.List;
import java.util.Optional;

public interface AssetRepository {
    Optional<Asset> findBySymbol(String symbol);
    List<Asset> findAll();
}
