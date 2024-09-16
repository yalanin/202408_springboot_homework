package com.yalanin.springboot_homework.controller;

import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetQueryParam;
import com.yalanin.springboot_homework.dto.AssetRequest;
import com.yalanin.springboot_homework.model.Asset;
import com.yalanin.springboot_homework.service.AssetService;
import com.yalanin.springboot_homework.swagger.AssetResponse;
import com.yalanin.springboot_homework.util.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Validated
@RestController
@Tag(name = "Asset API", description = "資產操作 API")
public class AssetController {
    @Autowired
    private AssetService assetService;

    @Operation(summary = "新增資產")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "資產創建成功",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AssetResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "資產創建失敗",
                    content = @Content
            )
    })
    @PostMapping("/users/{userId}/assets")
    public ResponseEntity<Asset> createAsset(
            @Parameter(name = "userId", description = "使用者 ID", required = true, example = "1")
            @PathVariable Integer userId,
            @RequestBody @Valid AssetCreateRequest assetCreateRequest) {
        Integer assetId = assetService.createAsset(userId, assetCreateRequest);
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.status(HttpStatus.CREATED).body(asset);
    }

    @Operation(summary = "取得資產")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "取得資產成功",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到使用者",
                    content = @Content
            )
    })
    @GetMapping("/users/{userId}/assets")
    public ResponseEntity<Page<Asset>> getAsset(
            @Parameter(name = "userId", description = "使用者 ID", required = true, example = "1")
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "10") @Max(100) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

        AssetQueryParam assetQueryParam = new AssetQueryParam();
        assetQueryParam.setUserId(userId);
        assetQueryParam.setLimit(limit);
        assetQueryParam.setOffset(offset);

        List<Asset> assetList = assetService.getAssetsByUserId(assetQueryParam);
        Integer count = assetService.countAssets(assetQueryParam);

        Page<Asset> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(assetList);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @Operation(summary = "更新資產")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "資產更新成功",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AssetResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "找不到資產",
                    content = @Content
            )
    })
    @PutMapping("/assets/{assetId}")
    public ResponseEntity<Asset> updateAsset(
            @Parameter(name = "assetId", description = "資產 ID", required = true, example = "1")
            @PathVariable Integer assetId,
            @RequestBody @Valid AssetRequest assetRequest) {
        Asset asset = assetService.getAssetById(assetId);
        if(asset == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            assetService.updateAsset(assetId, assetRequest);
            Asset updatedAsset = assetService.getAssetById(assetId);
            return ResponseEntity.status(HttpStatus.OK).body(updatedAsset);
        }
    }

    @Operation(summary = "刪除資產")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "已刪除資產",
                    content = @Content
            )
    })
    @DeleteMapping("/assets/{assetId}")
    public ResponseEntity<Asset> deleteAsset(
            @Parameter(name = "assetId", description = "資產 ID", required = true, example = "1")
            @PathVariable Integer assetId) {
        assetService.deleteAssetById(assetId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
