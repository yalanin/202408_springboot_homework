package com.yalanin.springboot_homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yalanin.springboot_homework.dao.UserDao;
import com.yalanin.springboot_homework.dto.UserRegisterRequest;
import com.yalanin.springboot_homework.dto.UserRequest;
import com.yalanin.springboot_homework.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    // 建立使用者成功
    @Test
    public void createUserSuccess() throws Exception{
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setPassword("12345678");
        userRegisterRequest.setEmail("test_create_user@example.com");
        userRegisterRequest.setUsername("test user");
        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.email", equalTo("test_create_user@example.com")))
                .andExpect(jsonPath("$.username", equalTo("test user")))
                .andExpect(jsonPath("$.created_at", notNullValue()))
                .andExpect(jsonPath("$.updated_at", notNullValue()));

        // 檢查資料庫中的密碼不為明碼
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        assertNotEquals(userRegisterRequest.getPassword(), user.getPassword());
    }

    // 建立使用者失敗
    @Test
    public void registerEmailAlreadyExist() throws Exception {
        // 先註冊一個帳號
        RequestBuilder requestBuilder = createUserRequest();
        createUser();
        // 再次使用同個 email 註冊
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    // 建立使用者參數格式錯誤失敗
    @Test
    public void registerInvalidEmailFormat() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setPassword("12345678");
        userRegisterRequest.setEmail("hdeuoiwuwj");
        userRegisterRequest.setUsername("test user");
        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    // 查詢使用者成功
    @Test
    public void getUserSuccess() throws Exception {
        // 先註冊一個帳號
        // createUser();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{userId}", 2);
        User user = userDao.getUserById(2);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo(user.getUsername())))
                .andExpect(jsonPath("$.email", equalTo(user.getEmail())))
                .andExpect(jsonPath("$.created_at", notNullValue()));
    }

    // 查詢不存在的使用者
    @Test
    public void getUserNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{userId}", 100);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(404));
    }

    // 更新使用者成功
    @Test
    public void updateUserSuccess() throws Exception {
        // 先註冊一個帳號
        // createUser();

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("aaa@example.com");
        userRequest.setUsername("update username");
        String json = objectMapper.writeValueAsString(userRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/users/{userId}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.email", equalTo("aaa@example.com")))
                .andExpect(jsonPath("$.username", equalTo("update username")));
    }

    // 更新使用者失敗
    @Test
    public void updateUserillegalArgument() throws Exception {
        // 先註冊一個帳號
        // createUser();

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("aaa");
        userRequest.setUsername("update username");
        String json = objectMapper.writeValueAsString(userRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/users/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    // 刪除使用者
    @Test
    public void deleteUserSuccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/{userId}", 1);

        mockMvc.perform(requestBuilder)
                .andExpect((status().is(204)));
    }

    private RequestBuilder createUserRequest() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setPassword("12345678");
        userRegisterRequest.setEmail("test_create_user123@example.com");
        userRegisterRequest.setUsername("test user");
        String json = objectMapper.writeValueAsString(userRegisterRequest);
        return MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }

    private void createUser() throws Exception {
        RequestBuilder requestBuilder = createUserRequest();
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));
    }
}