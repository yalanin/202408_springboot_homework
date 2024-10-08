package com.yalanin.springboot_homework.dao.impl;

import com.yalanin.springboot_homework.dao.AssetDao;
import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetQueryParam;
import com.yalanin.springboot_homework.dto.AssetRequest;
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
public class AssetDaoImpl implements AssetDao {
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
    public Asset getAssetById(Integer assetId) {
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
    public List<Integer> getAssetIdsByUserId(AssetQueryParam assetQueryParam) {
        String sql = "SELECT asset_id FROM assets WHERE user_id = :userId ";
        sql += "LIMIT :limit OFFSET :offset";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", assetQueryParam.getUserId());
        map.put("limit", assetQueryParam.getLimit());
        map.put("offset", assetQueryParam.getOffset());

        // 查詢 asset_id 並將結果存入 List<Integer>
        return namedParameterJdbcTemplate.queryForList(sql, map, Integer.class);
    }

    @Override
    public Integer countAssets(AssetQueryParam assetQueryParam) {
        String sql = "SELECT count(*) FROM assets WHERE user_id = :userId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", assetQueryParam.getUserId());
        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    @Override
    public void updateAsset(Integer assetId, AssetRequest assetRequest) {
        String sql = "UPDATE assets SET name = :name, amount = :amount WHERE asset_id = :assetId";
        Map<String, Object> map = new HashMap<>();
        map.put("assetId", assetId);
        map.put("name", assetRequest.getName());
        map.put("amount", assetRequest.getAmount());
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteAssetById(Integer assetId) {
        String sql = "DELETE FROM assets WHERE asset_id = :assetId";
        Map<String, Object> map = new HashMap<>();
        map.put("assetId", assetId);
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteAssetByUserId(Integer userId) {
        String sql = "DELETE FROM assets WHERE user_id = :userId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        namedParameterJdbcTemplate.update(sql, map);
    }
}
