package com.yalanin.springboot_homework.controller;

import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.model.Asset;
import com.yalanin.springboot_homework.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class AssetController {
    @Autowired
    private AssetService assetService;

    @PostMapping("/users/{userId}/assets")
    public ResponseEntity<Asset> createAsset(@PathVariable Integer userId,
                                             @RequestBody @Valid AssetCreateRequest assetCreateRequest) {
        Integer assetId = assetService.createAsset(userId, assetCreateRequest);
        Asset asset = assetService.findAssetById(assetId);
        return ResponseEntity.status(HttpStatus.OK).body(asset);
    }
}
