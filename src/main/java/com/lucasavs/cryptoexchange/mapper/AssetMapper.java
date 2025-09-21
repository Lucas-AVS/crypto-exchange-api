package com.lucasavs.cryptoexchange.mapper;

import com.lucasavs.cryptoexchange.dto.AssetDto;
import com.lucasavs.cryptoexchange.entity.Asset;
import org.springframework.stereotype.Component;

@Component
public class AssetMapper {
    public AssetDto toDto(Asset asset) {
        AssetDto dto = new AssetDto();
        dto.setSymbol(asset.getSymbol());
        dto.setName(asset.getName());
        dto.setType(asset.getKind());
        dto.setDecimalPlaces(asset.getScale());
        return dto;
    }

}
