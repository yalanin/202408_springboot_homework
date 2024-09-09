package com.yalanin.springboot_homework.dao.impl;

import com.yalanin.springboot_homework.dao.AssetDao;
import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetQueryParam;
import com.yalanin.springboot_homework.model.Asset;
import com.yalanin.springboot_homework.rowmapper.AssetRowmapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AssetDaoimpl implements AssetDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createAsset(Integer userId, AssetCreateRequest assetCreateRequest) {
        String sql = "INSERT INTO assets(user_id, name, amount, created_at, updated_at) " +
                "VALUES(:userId, :name, :amount, :createdAt, :updatedAt)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Date now = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("name", assetCreateRequest.getName());
        map.put("amount", assetCreateRequest.getAmount());
        map.put("createdAt", now);
        map.put("updatedAt", now);

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Asset findAssetById(Integer assetId) {
        String sql = "SELECT asset_id, user_id, name, amount, created_at, updated_at FROM assets "
                + "WHERE asset_id = :assetId";

        Map<String, Object> map = new HashMap<>();
        map.put("assetId", assetId);

        List<Asset> assetList = namedParameterJdbcTemplate.query(sql, map, new AssetRowmapper());

        if(!assetList.isEmpty()) {
            return assetList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Asset> getAssetsByUserId(AssetQueryParam assetQueryParam) {
        String sql = "SELECT asset_id, user_id, name, amount, created_at, updated_at FROM assets "
                + "WHERE user_id = :userId ";
        sql += "LIMIT :limit OFFSET :offset";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", assetQueryParam.getUserId());
        map.put("limit", assetQueryParam.getLimit());
        map.put("offset", assetQueryParam.getOffset());
        return namedParameterJdbcTemplate.query(sql, map, new AssetRowmapper());
    }

    @Override
    public Integer countAssets(AssetQueryParam assetQueryParam) {
        String sql = "SELECT count(*) FROM assets WHERE user_id = :userId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", assetQueryParam.getUserId());
        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }
}
