package com.yalanin.springboot_homework.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

public class AssetResponse {
    @Schema(description = "使用者 ID", example = "1")
    private Integer asset_id;

    @Schema(description = "使用者 ID", example = "1")
    private Integer user_id;

    @Schema(description = "資產金額", example = "100")
    private Integer amount;

    @Schema(description = "資產名稱", example = "test asset")
    private String name;

    @Schema(description = "創建日期", example = "2024-09-11 14:05:31")
    private String created_at;

    @Schema(description = "最後更新日期", example = "2024-09-11 14:05:31")
    private String update_at;

    public Integer getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(Integer asset_id) {
        this.asset_id = asset_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }
}
