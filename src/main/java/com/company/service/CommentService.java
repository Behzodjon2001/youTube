package com.company.service;

import com.company.dto.CommentDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.video.VideoDTO;
import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.video.VideoEntity;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private  ProfileService profileService;
    @Autowired
    private VideoService videoService;

    public CommentDTO create(CommentDTO dto, Integer profileId) {

        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setVisible(true);

        ProfileEntity profile = profileService.get(profileId);
        entity.setProfile(profile);

        VideoEntity video = videoService.get(dto.getVideo());
        entity.setVideo(video);

        commentRepository.save(entity);

        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public void update(CommentDTO dto, Integer profileId) {
        Optional<CommentEntity> optional = commentRepository.findById(dto.getId());
        if (optional.isEmpty()) {
            throw new BadRequestException("Not found");
        }
        ProfileEntity profile = profileService.get(profileId);

        if (!Objects.equals(profile.getId(), dto.getProfile().getId())) {
            log.error("Mazgi bu sening commenting emas {}" , dto);
            throw new BadRequestException("Mazgi bu sening commenting emas");
        }

        CommentEntity entity = optional.get();
        entity.setContent(dto.getContent());
        entity.setUpdateDate(LocalDateTime.now());
        commentRepository.save(entity);
    }

    public List<CommentDTO> list(VideoDTO dto) {
        boolean existArticle = videoService.isExistArticle(dto.getId());
        if (!existArticle) {
            log.error("Article not found {}" , dto);
            throw new ItemNotFoundException("Video not found");
        }
        VideoEntity entity = new VideoEntity(dto.getId());
        List<CommentEntity> commentEntityList = commentRepository.findByVideo(entity);

        return entityToDtoList(commentEntityList);
    }

    private List<CommentDTO> entityToDtoList(List<CommentEntity> entityList) {
        List<CommentDTO> list = new LinkedList<>();
        for (CommentEntity ent : entityList) {
            CommentDTO dto1 = new CommentDTO();
            dto1.setContent(ent.getContent());
            dto1.setCreatedDate(ent.getCreatedDate());

            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setName(ent.getProfile().getName());
            profileDTO.setSurname(ent.getProfile().getSurname());
            profileDTO.setEmail(ent.getProfile().getEmail());

            dto1.setProfile(profileDTO);

            dto1.setVideo(ent.getVideo().getId());

            list.add(dto1);
        }
        return list;
    }

    public void delete(Integer id) {
        Optional<CommentEntity> optional = commentRepository.findById(id);
        changeVisible(optional);
    }

    public void delete(Integer profileId, Integer id) {
        Optional<CommentEntity> optional = commentRepository.findByIdAndProfileId(id, profileId);
        changeVisible(optional);
    }

    public CommentEntity get(Integer id) {
        return commentRepository.findById(id).orElseThrow(() -> {
            log.error("Comment not found {}" , id);
            throw new BadRequestException("Comment not found");
        });
    }

    private void changeVisible(Optional<CommentEntity> optional) {
        if (optional.isEmpty()) {
            log.error("Something went wrong {}" , optional);
            throw new BadRequestException("Something went wrong");
        }
        CommentEntity entity = optional.get();
        entity.setVisible(false);
        commentRepository.save(entity);
    }
}
