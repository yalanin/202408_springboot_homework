package com.yalanin.springboot_homework.dao;

import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.model.Asset;

public interface AssetDao {
    Integer createAsset(Integer userId, AssetCreateRequest assetCreateRequest);

    Asset findAssetById(Integer assetId);
}
