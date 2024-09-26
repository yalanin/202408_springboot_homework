package com.yalanin.springboot_homework.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yalanin.springboot_homework.dao.AssetDao;
import com.yalanin.springboot_homework.dao.UserDao;
import com.yalanin.springboot_homework.dto.UserRegisterRequest;
import com.yalanin.springboot_homework.dto.UserRequest;
import com.yalanin.springboot_homework.model.User;
import com.yalanin.springboot_homework.redis.RedisService;
import com.yalanin.springboot_homework.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private AssetDao assetDao;

    @Autowired
    private RedisService redisService;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查電子信箱是否使用過
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        if(user != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 加密密碼
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        Integer userId = userDao.createUser(userRegisterRequest);
        // 把資料存到 redis 裡面
        saveUserToRedis(userId);
        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        Object redirsUser = redisService.getValue("user_" + userId);

        if(redirsUser == null) {
            log.warn("redis 找不到 user");
            // 把資料存到 redis 裡面
            saveUserToRedis(userId);
            redirsUser = redisService.getValue("user_" + userId);
        }

        log.warn("回傳 redis user");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(redirsUser, User.class);
    }

    @Override
    public void updateUser(Integer userId, UserRequest userRequest) {
        userDao.updateUser(userId, userRequest);
        // 把更新的資料存到 redis 裡面
        saveUserToRedis(userId);
    }

    @Transactional
    @Override
    public void deleteUserById(Integer userId) {
        // 使用者刪除後，名下相關資產也應該跟著刪除
        assetDao.deleteAssetByUserId(userId);
        userDao.deleteUserById(userId);
        // 刪除 redis 裡的資料
        redisService.deleteValue("user_" + userId);
    }

    // 把資料存到 redis 裡面
    private void saveUserToRedis(Integer userId) {
        User user = userDao.getUserById(userId);
        if(user != null) {
            redisService.setValue("user_" + userId, user);
        }
    }
}
