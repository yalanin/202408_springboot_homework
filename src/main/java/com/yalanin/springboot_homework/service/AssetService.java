package com.yalanin.springboot_homework.service;

import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetQueryParam;
import com.yalanin.springboot_homework.dto.AssetRequest;
import com.yalanin.springboot_homework.model.Asset;

import java.util.List;

public interface AssetService {
    Integer createAsset(Integer userId, AssetCreateRequest assetCreateRequest);

    Integer countAssets(AssetQueryParam assetQueryParam);

    Asset getAssetById(Integer assetId);

    List<Asset> getAssetsByUserId(AssetQueryParam assetQueryParam);

    void updateAsset(Integer assetId, AssetRequest assetRequest);
}
