package com.yalanin.springboot_homework.controller;

import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetQueryParam;
import com.yalanin.springboot_homework.dto.AssetRequest;
import com.yalanin.springboot_homework.model.Asset;
import com.yalanin.springboot_homework.service.AssetService;
import com.yalanin.springboot_homework.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Validated
@RestController
public class AssetController {
    @Autowired
    private AssetService assetService;

    @PostMapping("/users/{userId}/assets")
    public ResponseEntity<Asset> createAsset(@PathVariable Integer userId,
                                             @RequestBody @Valid AssetCreateRequest assetCreateRequest) {
        Integer assetId = assetService.createAsset(userId, assetCreateRequest);
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.status(HttpStatus.OK).body(asset);
    }

    @GetMapping("/users/{userId}/assets")
    public ResponseEntity<Page<Asset>> getAsset(@PathVariable Integer userId,
                                                @RequestParam(defaultValue = "10") @Max(100) @Min(0) Integer limit,
                                                @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

        AssetQueryParam assetQueryParam = new AssetQueryParam();
        assetQueryParam.setUserId(userId);
        assetQueryParam.setLimit(limit);
        assetQueryParam.setOffset(offset);

        List<Asset> assetList = assetService.getAssetsByUserId(assetQueryParam);
        Integer count = assetService.countAssets(assetQueryParam);

        Page<Asset> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(assetList);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @PutMapping("/assets/{assetId}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Integer assetId,
                                             @RequestBody @Valid AssetRequest assetRequest) {
        Asset asset = assetService.getAssetById(assetId);
        if(asset == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            assetService.updateAsset(assetId, assetRequest);
            Asset updatedAsset = assetService.getAssetById(assetId);
            return ResponseEntity.status(HttpStatus.OK).body(updatedAsset);
        }
    }

    @DeleteMapping("/assets/{assetId}")
    public ResponseEntity<Asset> deleteAsset(@PathVariable Integer assetId) {
        assetService.deleteAssetById(assetId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
