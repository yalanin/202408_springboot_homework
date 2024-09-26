package com.yalanin.springboot_homework.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yalanin.springboot_homework.dao.AssetDao;
import com.yalanin.springboot_homework.dao.UserDao;
import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetQueryParam;
import com.yalanin.springboot_homework.dto.AssetRequest;
import com.yalanin.springboot_homework.model.Asset;
import com.yalanin.springboot_homework.model.User;
import com.yalanin.springboot_homework.redis.RedisService;
import com.yalanin.springboot_homework.service.AssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssetServiceImpl implements AssetService {
    private final static Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);
    @Autowired
    private AssetDao assetDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisService redisService;

    @Transactional
    @Override
    public Integer createAsset(Integer userId, AssetCreateRequest assetCreateRequest) {
        User user = userDao.getUserById(userId);
        // 檢查使用者是否存在
        if(user == null) {
            log.warn("========");
            log.warn("使用者 ID {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 用 asset id 儲存 資產
        Integer assetId = assetDao.createAsset(userId, assetCreateRequest);
        saveAssetToRedis(assetId);
        return assetId;
    }

    @Override
    public List<Asset> getAssetsByUserId(AssetQueryParam assetQueryParam) {
        User user = userDao.getUserById(assetQueryParam.getUserId());
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 因為每個 asset 可能都會修改刪除，所以不能用 list 存內容，以免沒過期但是資料已經有變動，改成用 asset id 拿資料
        List<Integer> assetIds = assetDao.getAssetIdsByUserId(assetQueryParam);
        List<Asset> assetList = new ArrayList<>();

        for (int assetId : assetIds) {
            log.warn("assetId: " + assetId);

            Object redisAsset = redisService.getValue("asset_" + assetId);

            log.warn("redisAsset: " + redisAsset);

            if(redisAsset == null) {
                // 用 asset id 儲存 資產
                saveAssetToRedis(assetId);
                redisAsset = redisService.getValue("asset_" + assetId);
            }
            // 避免很倒霉剛好有人把 asset 刪掉
            if(redisAsset != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                assetList.add(objectMapper.convertValue(redisAsset, Asset.class));
            }
        }
        return assetList;
    }

    @Override
    public Integer countAssets(AssetQueryParam assetQueryParam) {
        return assetDao.countAssets(assetQueryParam);
    }

    @Override
    public Asset getAssetById(Integer assetId) {
        Object redisAsset = redisService.getValue("asset_" + assetId);
        if(redisAsset == null) {
            // 用 asset id 儲存 資產
            saveAssetToRedis(assetId);
            redisAsset = redisService.getValue("asset_" + assetId);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(redisAsset, Asset.class);
    }

    @Transactional
    @Override
    public void updateAsset(Integer assetId, AssetRequest assetRequest) {
        assetDao.updateAsset(assetId, assetRequest);
        // 用 asset id 儲存 變動後的資產
        saveAssetToRedis(assetId);
    }

    @Transactional
    @Override
    public void deleteAssetById(Integer assetId) {
        assetDao.deleteAssetById(assetId);
        // 刪除 redis 中的紀錄
        redisService.deleteValue("asset_" + assetId);
    }

    private void saveAssetToRedis(Integer assetId) {
        Asset asset = assetDao.getAssetById(assetId);
        redisService.setValue("asset_" + assetId, asset);
    }
}
