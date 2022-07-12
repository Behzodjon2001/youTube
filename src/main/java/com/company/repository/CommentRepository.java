package com.company.repository;

import com.company.dto.CommentDTO;
import com.company.entity.CommentEntity;
import com.company.entity.video.VideoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity, Integer> {
    List<CommentEntity> findByVideo(VideoEntity entity);

    Optional<CommentEntity> findByIdAndProfileId(Integer id, Integer profileId);

}
