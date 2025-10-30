package com.lucasavs.wallet.client;

import com.lucasavs.wallet.dto.AssetDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AssetServiceClient {

    private final RestTemplate restTemplate;
    private final String assetServiceUrl;

    public AssetServiceClient(RestTemplate restTemplate, @Value("${endpoint.assets}") String assetServiceUrl) {
        this.restTemplate = restTemplate;
        this.assetServiceUrl = assetServiceUrl;
    }

    public Optional<AssetDto> findAssetBySymbol(String assetSymbol) {
        String url = assetServiceUrl + "/api/v1/assets/" + assetSymbol;
        try {
            AssetDto asset = restTemplate.getForObject(url, AssetDto.class);
            return Optional.ofNullable(asset);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}
