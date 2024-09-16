package com.yalanin.springboot_homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema
public class UserRequest {
    @Schema(description = "使用者名稱", example = "john_doe")
    @NotBlank
    private String username;

    @Schema(description = "電子信箱，必須為唯一值", example = "john@example.com")
    @NotNull
    @Email
    private String email;

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
}
