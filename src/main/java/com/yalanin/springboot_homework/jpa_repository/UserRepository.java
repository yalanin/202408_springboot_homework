package com.yalanin.springboot_homework.jpa_repository;

import com.yalanin.springboot_homework.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
