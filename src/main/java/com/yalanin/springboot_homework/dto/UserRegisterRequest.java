package com.yalanin.springboot_homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema
public class UserRegisterRequest {
    @Schema(description = "使用者名稱", example = "john_doe")
    @NotBlank
    private String username;

    @Schema(description = "電子信箱，必須為唯一值", example = "john@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "密碼", example = "password123")
    @NotBlank
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
