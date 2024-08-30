package com.yalanin.springboot_homework.service;

import com.yalanin.springboot_homework.dto.UserRegisterRequest;
import com.yalanin.springboot_homework.dto.UserRequest;
import com.yalanin.springboot_homework.model.User;

public interface UserService {
    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    void updateUser(Integer userId, UserRequest userRequest);

    void deleteUserById(Integer userId);
}
