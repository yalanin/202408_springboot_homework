package com.yalanin.springboot_homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yalanin.springboot_homework.dto.AssetCreateRequest;
import com.yalanin.springboot_homework.dto.AssetRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(AssetControllerTest.class);

    // 新增資產
    @Test
    public void createAssetSuccess() throws Exception {
        AssetCreateRequest assetCreateRequest = new AssetCreateRequest();
        assetCreateRequest.setAmount(1000);
        assetCreateRequest.setName("test asset");
        String json = objectMapper.writeValueAsString(assetCreateRequest);

        logger.info("Finished testLogging");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/assets", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name", equalTo("test asset")))
                .andExpect(jsonPath("$.amount", equalTo(1000)));
    }

    // 新增資產失敗
    @Test
    public void createAssetInvalidParams() throws Exception {
        AssetCreateRequest assetCreateRequest = new AssetCreateRequest();
        assetCreateRequest.setAmount(-10);
        assetCreateRequest.setName("test asset");
        String json = objectMapper.writeValueAsString(assetCreateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/{userId}/assets", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    // 取得資產
    @Test
    public void getAssetSuccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{userId}/assets", 1);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()));
    }

    // 更新資產成功
    @Test
    public void updateAssetSuccess() throws Exception {
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setAmount(10);
        assetRequest.setName("updated name");
        String json = objectMapper.writeValueAsString(assetRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/assets/{assetId}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", equalTo(10)))
                .andExpect(jsonPath("$.name", equalTo("updated name")));
    }

    // 更新資產失敗
    @Test
    public void updateAssetIllegalParams() throws Exception {
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setAmount(10);
        String json = objectMapper.writeValueAsString(assetRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/assets/{assetId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    // 刪除資產
    @Test
    public void deleteAssetSuccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/assets/{assetId}", 1);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(204));
    }
}