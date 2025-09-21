package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.dto.AssetDto;
import java.util.List;

public interface AssetService {
    List<AssetDto> findAll();
    AssetDto findBySymbol(String symbol);
}
