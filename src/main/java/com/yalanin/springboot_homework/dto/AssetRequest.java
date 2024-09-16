package com.yalanin.springboot_homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema
public class AssetRequest {
    @Schema(description = "資產名稱", example = "BitCoin")
    @NotBlank
    private String name;

    @Schema(description = "資產金額", example = "100")
    @NotNull
    @PositiveOrZero
    private Integer amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
