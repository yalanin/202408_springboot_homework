package com.yalanin.springboot_homework.rowmapper;

import com.yalanin.springboot_homework.model.Asset;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AssetRowmapper implements RowMapper<Asset> {
    @Override
    public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
        Asset asset = new Asset();
        asset.setAsset_id(rs.getInt("asset_id"));
        asset.setAmount(rs.getInt("amount"));
        asset.setCreatedAt(rs.getTimestamp("created_at"));
        asset.setUpdatedAt(rs.getTimestamp("updated_at"));
        return asset;
    }
}
