package com.yalanin.springboot_homework.service.impl;

import com.yalanin.springboot_homework.dao.AssetDao;
import com.yalanin.springboot_homework.dao.UserDao;
import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetQueryParam;
import com.yalanin.springboot_homework.dto.AssetRequest;
import com.yalanin.springboot_homework.jpa_repository.AssetRepository;
import com.yalanin.springboot_homework.jpa_repository.UserRepository;
import com.yalanin.springboot_homework.model.Asset;
import com.yalanin.springboot_homework.model.User;
import com.yalanin.springboot_homework.service.AssetService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssetDao assetDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CacheManager cacheManager;

    @Transactional
    @Override
    public Integer createAsset(Integer userId, AssetCreateRequest assetCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Asset newAsset = modelMapper.map(assetCreateRequest, Asset.class);
        Asset createdAsset = assetRepository.save(newAsset);
        return createdAsset.getAsset_id();
    }

    @Override
    public List<Asset> getAssetsByUserId(AssetQueryParam assetQueryParam) {
        User user = userRepository.findById(assetQueryParam.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // 因為每個 asset 可能都會修改刪除，所以不能用 list 存內容，以免沒過期但是資料已經有變動，改成用 asset id 拿資料
        Pageable pageable = PageRequest.of(assetQueryParam.getOffset() / assetQueryParam.getLimit(), assetQueryParam.getLimit());
        List<Integer> assetIds = assetRepository.findAssetIdsByUserId(assetQueryParam.getUserId(), pageable);
        List<Asset> assetList = new ArrayList<>();
        Cache cache = cacheManager.getCache("assetCache");

        for (int assetId : assetIds) {
            Asset redisAsset = cache.get(assetId, Asset.class);

            if(redisAsset != null) {
                assetList.add(redisAsset);
            } else {
                Asset asset = assetDao.getAssetById(assetId);
                cache.put(assetId, asset);
                assetList.add(asset);
            }
        }
        return assetList;
    }

    @Override
    public Integer countAssets(AssetQueryParam assetQueryParam) {
        return assetRepository.countByUserUserId(assetQueryParam.getUserId());
    }

    @Cacheable(value = "assetCache", key = "#assetId")
    @Override
    public Asset getAssetById(Integer assetId) {
        return saveAssetToRedis(assetId);
    }

    @CacheEvict(value = "assetCache", key = "#assetId")
    @Transactional
    @Override
    public void updateAsset(Integer assetId, AssetRequest assetRequest) {
        Asset updatedAsset = assetRepository.findById(assetId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(assetRequest, updatedAsset);
    }

    @CacheEvict(value = "assetCache", key = "#assetId")
    @Transactional
    @Override
    public void deleteAssetById(Integer assetId) {
        assetRepository.deleteById(assetId);
    }

    // 把資料存到 redis 裡面
    @CachePut(value = "assetCache", key = "#assetId")
    private Asset saveAssetToRedis(Integer assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
