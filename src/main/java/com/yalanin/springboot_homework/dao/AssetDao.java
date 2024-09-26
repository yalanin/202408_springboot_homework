package com.yalanin.springboot_homework.dao;

import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetQueryParam;
import com.yalanin.springboot_homework.dto.AssetRequest;
import com.yalanin.springboot_homework.model.Asset;

import java.util.List;

public interface AssetDao {
    Integer createAsset(Integer userId, AssetCreateRequest assetCreateRequest);

    Integer countAssets(AssetQueryParam assetQueryParam);

    Asset getAssetById(Integer assetId);

    List<Asset> getAssetsByUserId(AssetQueryParam assetQueryParam);

    List<Integer> getAssetIdsByUserId(AssetQueryParam assetQueryParam);

    void updateAsset(Integer assetId, AssetRequest assetRequest);

    void deleteAssetById(Integer assetId);

    void deleteAssetByUserId(Integer userId);
}
