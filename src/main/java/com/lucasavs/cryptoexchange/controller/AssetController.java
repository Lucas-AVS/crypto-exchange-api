package com.lucasavs.cryptoexchange.controller;

import com.lucasavs.cryptoexchange.dto.AssetDto;
import com.lucasavs.cryptoexchange.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AssetController {
    private final AssetService assetService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/assets")
    public ResponseEntity<List<AssetDto>> findAllAssets() {
        return ResponseEntity.ok(assetService.findAll());
    }

    @GetMapping("/assets/{assetSymbol}")
    public ResponseEntity<AssetDto> getAsset(@PathVariable String assetSymbol) {
        return ResponseEntity.ok(assetService.findBySymbol(assetSymbol));
    }
}