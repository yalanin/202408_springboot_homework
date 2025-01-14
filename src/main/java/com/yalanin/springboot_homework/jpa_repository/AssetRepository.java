package com.yalanin.springboot_homework.jpa_repository;

import com.yalanin.springboot_homework.model.Asset;
import org.springframework.data.repository.CrudRepository;

public interface AssetRepository extends CrudRepository<Asset, Integer> {
    void deleteByUserId(Integer userId);
}