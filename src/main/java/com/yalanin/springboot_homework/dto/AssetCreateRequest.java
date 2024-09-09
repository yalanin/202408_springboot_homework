package com.yalanin.springboot_homework.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class AssetCreateRequest {
    @NotNull
    private String name;

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
