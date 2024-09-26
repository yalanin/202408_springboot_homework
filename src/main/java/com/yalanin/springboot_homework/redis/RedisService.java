package com.yalanin.springboot_homework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    // 時間格式預設是分鐘
    private final TimeUnit defaultTimeUnit = TimeUnit.MINUTES;
    // 預設過期時間是十分鐘
    private final int timeout = 10;

    // 可以指定時間格式
    public <T> void setValue(String key, Object value, long timeout, TimeUnit unit) {
        // 設定物件的時候就直接賦予過期時間
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 使用預設的時間格式以及預設的時間
    public <T> void setValue(String key, Object value) {
        // 設定物件的時候就直接賦予過期時間
        redisTemplate.opsForValue().set(key, value, timeout, defaultTimeUnit);
    }

    public <T> Object getValue(String key) {
       return redisTemplate.opsForValue().get(key);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}
