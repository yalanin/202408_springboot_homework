package com.yalanin.springboot_homework.dao.impl;

import com.yalanin.springboot_homework.dao.UserDao;
import com.yalanin.springboot_homework.dto.UserRegisterRequest;
import com.yalanin.springboot_homework.dto.UserRequest;
import com.yalanin.springboot_homework.model.User;
import com.yalanin.springboot_homework.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id, username, email, password, created_at, updated_at FROM users" +
                " WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        return returnUserByQuery(sql, map);
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, username, email, password, created_at, updated_at FROM users" +
                " WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        return returnUserByQuery(sql, map);
    }

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO users(username, email, password, created_at, updated_at) " +
               "VALUES(:username, :email, :password, :createdAt, :updatedAt)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Date now = new Date();

        Map<String, Object>  map = new HashMap<>();
        map.put("username", userRegisterRequest.getUsername());
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());
        map.put("createdAt", now);
        map.put("updatedAt", now);

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public void updateUser(Integer userId, UserRequest userRequest) {
        String sql = "UPDATE users SET email = :email, username = :username WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("email", userRequest.getEmail());
        map.put("username", userRequest.getUsername());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteUserById(Integer userId) {
        String sql = "DELETE FROM users WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    // 統一執行 sql 語法並回傳結果
    private User returnUserByQuery(String sql, Map map) {
        List<User> list = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        if(!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
