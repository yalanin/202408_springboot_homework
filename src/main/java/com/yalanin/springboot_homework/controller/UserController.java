package com.yalanin.springboot_homework.controller;

import com.yalanin.springboot_homework.dto.UserRegisterRequest;
import com.yalanin.springboot_homework.dto.UserRequest;
import com.yalanin.springboot_homework.model.User;
import com.yalanin.springboot_homework.service.UserService;
import com.yalanin.springboot_homework.swagger.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "使用者操作 API")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "新增使用者")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "使用者創建成功",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "使用者創建失敗",
                    content = @Content
            )
    })
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        Integer userId = userService.register(userRegisterRequest);
        User user = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "取得使用者資訊")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "使用者資訊",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "查無該使用者",
                    content = @Content
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(
            @Parameter(name = "userId", description = "使用者 ID", required = true, example = "1")
            @PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        if(user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "更新使用者資訊")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "更新後的使用者資訊",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "查無該使用者",
                    content = @Content
            )
    })
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @Parameter(name = "userId", description = "使用者 ID", required = true, example = "1")
            @PathVariable Integer userId,
            @RequestBody @Valid UserRequest userRequest) {
        User user = userService.getUserById(userId);
        if(user != null) {
            userService.updateUser(userId, userRequest);
            User updatedUser = userService.getUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "刪除使用者")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "已刪除使用者",
                    content = @Content
            )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUser(
            @Parameter(name = "userId", description = "使用者 ID", required = true, example = "1")
            @PathVariable Integer userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
