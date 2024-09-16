package com.yalanin.springboot_homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema
public class AssetCreateRequest {
    @Schema(description = "資產名稱", example = "BitCoin")
    @NotBlank
    private String name;

    @Schema(description = "資產金額", example = "1000")
    @NotNull
    @PositiveOrZero
    private Integer amount;

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull @PositiveOrZero Integer getAmount() {
        return amount;
    }

    public void setAmount(@NotNull @PositiveOrZero Integer amount) {
        this.amount = amount;
    }
}
