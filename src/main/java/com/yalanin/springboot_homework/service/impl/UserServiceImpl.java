package com.yalanin.springboot_homework.service.impl;

import com.yalanin.springboot_homework.dao.AssetDao;
import com.yalanin.springboot_homework.dao.UserDao;
import com.yalanin.springboot_homework.dto.UserRegisterRequest;
import com.yalanin.springboot_homework.dto.UserRequest;
import com.yalanin.springboot_homework.model.User;
import com.yalanin.springboot_homework.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

        return  userDao.createUser(userRegisterRequest);
    }

    @Cacheable(value = "userCache", key = "#userId")
    @Override
    public User getUserById(Integer userId) {
        return saveUserToRedis(userId);
    }

    @CacheEvict(value = "userCache", key = "#userId")
    @Override
    public void updateUser(Integer userId, UserRequest userRequest) {
        userDao.updateUser(userId, userRequest);
    }

    @CacheEvict(value = "userCache", key = "#userId")
    @Transactional
    @Override
    public void deleteUserById(Integer userId) {
        // 使用者刪除後，名下相關資產也應該跟著刪除
        assetDao.deleteAssetByUserId(userId);
        userDao.deleteUserById(userId);
    }

    // 把資料存到 redis 裡面
    @CachePut(value = "userCache", key = "#userId")
    private User saveUserToRedis(Integer userId) {
        return userDao.getUserById(userId);
    }
}
