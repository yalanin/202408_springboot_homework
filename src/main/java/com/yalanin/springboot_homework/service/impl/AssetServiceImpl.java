package com.yalanin.springboot_homework.service.impl;

import com.yalanin.springboot_homework.dao.AssetDao;
import com.yalanin.springboot_homework.dao.UserDao;
import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetQueryParam;
import com.yalanin.springboot_homework.dto.AssetRequest;
import com.yalanin.springboot_homework.model.Asset;
import com.yalanin.springboot_homework.model.User;
import com.yalanin.springboot_homework.service.AssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class AssetServiceImpl implements AssetService {
    private final static Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);
    @Autowired
    private AssetDao assetDao;

    @Autowired
    private UserDao userDao;

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
        return assetDao.createAsset(userId, assetCreateRequest);
    }

    @Override
    public List<Asset> getAssetsByUserId(AssetQueryParam assetQueryParam) {
        User user = userDao.getUserById(assetQueryParam.getUserId());
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return assetDao.getAssetsByUserId(assetQueryParam);
    }

    @Override
    public Integer countAssets(AssetQueryParam assetQueryParam) {
        return assetDao.countAssets(assetQueryParam);
    }

    @Override
    public Asset getAssetById(Integer assetId) {
        return assetDao.getAssetById(assetId);
    }

    @Transactional
    @Override
    public void updateAsset(Integer assetId, AssetRequest assetRequest) {
        assetDao.updateAsset(assetId, assetRequest);
    }

    @Transactional
    @Override
    public void deleteAssetById(Integer assetId) {
        assetDao.deleteAssetById(assetId);
    }
}
