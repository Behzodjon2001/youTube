package com.company.repository;

import com.company.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface PlaylistRepository extends PagingAndSortingRepository<PlaylistEntity, Integer> {

    boolean existsByIdAndVisible(Integer id, Boolean visible);



    @Transactional
    @Modifying
    @Query("update PlaylistEntity set visible = false where id = ?1")
    void changeStatus(Integer id);
}
