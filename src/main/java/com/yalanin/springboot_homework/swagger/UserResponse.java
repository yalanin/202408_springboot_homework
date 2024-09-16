package com.yalanin.springboot_homework.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponse {
    @Schema(description = "使用者 ID", example = "1")
    private Integer user_id;

    @Schema(description = "使用者名稱", example = "test user")
    private String username;

    @Schema(description = "電子信箱", example = "test@example.com")
    private String email;

    @Schema(description = "創建日期", example = "2024-09-11 14:05:31")
    private String created_at;

    @Schema(description = "最後更新日期", example = "2024-09-11 14:05:31")
    private String updated_at;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
