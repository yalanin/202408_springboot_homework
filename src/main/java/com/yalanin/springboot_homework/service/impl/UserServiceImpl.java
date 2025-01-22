package com.yalanin.springboot_homework.service.impl;

import com.yalanin.springboot_homework.dto.UserRegisterRequest;
import com.yalanin.springboot_homework.dto.UserRequest;
import com.yalanin.springboot_homework.jpa_repository.AssetRepository;
import com.yalanin.springboot_homework.jpa_repository.UserRepository;
import com.yalanin.springboot_homework.model.User;
import com.yalanin.springboot_homework.service.UserService;
import org.modelmapper.ModelMapper;
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
    private UserRepository userRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查電子信箱是否使用過
        User user = userRepository.findByEmail(userRegisterRequest.getEmail());
        if(user != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 加密密碼
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        User newUser = modelMapper.map(userRegisterRequest, User.class);
        User createdUser = userRepository.save(newUser);
        return createdUser.getUserId();
    }

    @Cacheable(value = "userCache", key = "#userId")
    @Override
    public User getUserById(Integer userId) {
        return saveUserToRedis(userId);
    }

    @CacheEvict(value = "userCache", key = "#userId")
    @Override
    public void updateUser(Integer userId, UserRequest userRequest) {
        User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(userRequest, updatedUser);
        userRepository.save(updatedUser);
    }

    @CacheEvict(value = "userCache", key = "#userId")
    @Transactional
    @Override
    public void deleteUserById(Integer userId) {
        // 使用者刪除後，名下相關資產也應該跟著刪除
        assetRepository.deleteByUser_UserId(userId);
        userRepository.deleteById(userId);
    }

    // 把資料存到 redis 裡面
    @CachePut(value = "userCache", key = "#userId")
    private User saveUserToRedis(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
