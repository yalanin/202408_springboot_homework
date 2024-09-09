package com.yalanin.springboot_homework.service;

import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.model.Asset;

public interface AssetService {
    Integer createAsset(Integer userId, AssetCreateRequest assetCreateRequest);

    Asset findAssetById(Integer assetId);
}
