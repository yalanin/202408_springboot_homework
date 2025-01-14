package com.yalanin.springboot_homework.jpa_repository;

import com.yalanin.springboot_homework.model.Asset;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetRepository extends CrudRepository<Asset, Integer> {
    @Query("SELECT a.id FROM Asset a WHERE a.user.id = :userId")
    List<Integer> findAssetIdsByUserId(@Param("userId") Integer userId, Pageable pageable);

    int countByUserUserId(Integer userId);

    void deleteByUser_UserId(Integer userId);
}