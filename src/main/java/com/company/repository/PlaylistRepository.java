package com.company.repository;

import com.company.entity.ChannelEntity;
import com.company.entity.PlaylistEntity;
import com.company.enums.ChannelStatus;
import com.company.enums.PlaylistStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface PlaylistRepository extends PagingAndSortingRepository<PlaylistEntity, Integer> {

    boolean existsByIdAndVisible(Integer id, Boolean visible);

    @Transactional
    @Modifying
    @Query("update PlaylistEntity set visible = false where id = ?1")
    void changeStatus(Integer id);

    Optional<PlaylistEntity> findById(Integer cId);

    Optional<PlaylistEntity> findByVisible(boolean b);
}
