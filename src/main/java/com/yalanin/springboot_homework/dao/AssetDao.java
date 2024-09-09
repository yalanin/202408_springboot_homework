package com.yalanin.springboot_homework.dao;

import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetQueryParam;
import com.yalanin.springboot_homework.model.Asset;

import java.util.List;

public interface AssetDao {
    Integer createAsset(Integer userId, AssetCreateRequest assetCreateRequest);

    Asset findAssetById(Integer assetId);

    List<Asset> getAssetsByUserId(AssetQueryParam assetQueryParam);

    Integer countAssets(AssetQueryParam assetQueryParam);
}
