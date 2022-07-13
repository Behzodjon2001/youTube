package com.company.repository;

import com.company.entity.CategoryEntity;
import com.company.entity.video.VideoTagEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VideoTagRepository extends CrudRepository<VideoTagEntity, Integer> {

    Iterable<VideoTagEntity> findByVideo(String videoId);
}
