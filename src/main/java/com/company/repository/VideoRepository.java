package com.company.repository;

import com.company.entity.video.VideoEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface VideoRepository extends PagingAndSortingRepository<VideoEntity, String> {
    Optional<VideoEntity> findByKey(String id);
}
