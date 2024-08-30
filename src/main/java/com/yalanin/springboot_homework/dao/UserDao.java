package com.yalanin.springboot_homework.dao;

import com.yalanin.springboot_homework.dto.UserRegisterRequest;
import com.yalanin.springboot_homework.model.User;

public interface UserDao {
    User getUserById(Integer userId);
    User getUserByEmail(String email);
    Integer createUser(UserRegisterRequest userRegisterRequest);
}
