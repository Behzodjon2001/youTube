package com.company.service;

import com.company.dto.PlaylistDTO;
import com.company.dto.video.VideoDTO;
import com.company.entity.CategoryEntity;
import com.company.entity.ChannelEntity;
import com.company.entity.PlaylistEntity;
import com.company.entity.video.VideoEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.attach.AttachEntity;
import com.company.enums.VideoStatus;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class VideoService {
    @Autowired
    private AttachService attachService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private VideoRepository videoRepository;

    public VideoDTO create(VideoDTO dto, Integer profileId) {
        VideoEntity entity = new VideoEntity();
        entity.setTitle(dto.getTitle());
        entity.setKey(dto.getKey());
        entity.setDescription(dto.getDescription());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setPublishedDate(null);
        entity.setStatus(dto.getStatus());
        entity.setType(dto.getType());

        AttachEntity attach = attachService.get(dto.getAttach());
        entity.setAttach(attach);

        AttachEntity review = attachService.get(dto.getReview());
        entity.setReview(review);

        ChannelEntity channel = channelService.get(dto.getChannel());
        entity.setChannel(channel);

        CategoryEntity category = categoryService.get(dto.getCategory());
        entity.setCategory(category);


        videoRepository.save(entity);

        VideoDTO articleDTO = new VideoDTO();
        articleDTO.setTitle(entity.getTitle());
        articleDTO.setKey(entity.getKey());
        articleDTO.setDescription(entity.getDescription());
        //  articleDTO.setRegionEntity(entity.getRegion());
        articleDTO.setStatus(entity.getStatus());
        // articleDTO.setCategoryEntity(entity.getCategory());
        //  articleDTO.setModerator(entity.getModerator());

        return articleDTO;
    }

    public VideoEntity fullUpdate(String id, VideoDTO dto, Integer profileId) {
        Optional<VideoEntity> optional = videoRepository.findByKey(id);
        if (optional.isEmpty()) {
            log.error("Video not found {}" , id);
            throw new ItemNotFoundException("This channel not found!");
        }
        VideoEntity entity = optional.get();

        entity.setTitle(dto.getTitle());
        entity.setKey(dto.getKey());
        entity.setDescription(dto.getDescription());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setPublishedDate(null);
        entity.setType(dto.getType());

        AttachEntity attach = attachService.get(dto.getAttach());
        entity.setAttach(attach);

        AttachEntity review = attachService.get(dto.getReview());
        entity.setReview(review);

        ChannelEntity channel = channelService.get(dto.getChannel());
        entity.setChannel(channel);

        CategoryEntity category = categoryService.get(dto.getCategory());
        entity.setCategory(category);

        return videoRepository.save(entity);
    }

    public VideoEntity changeVideoStatus(String id, VideoDTO dto) {
        Optional<VideoEntity> optional = videoRepository.findByKey(id);
        if (optional.isEmpty()) {
            log.error("Video not found {}" , id);
            throw new ItemNotFoundException("This channel not found!");
        }
        VideoEntity entity = optional.get();

        entity.setStatus(dto.getStatus());

        return videoRepository.save(entity);
    }

    public VideoEntity IncreaseVideoViewCountByKey(String id) {
        Optional<VideoEntity> optional = videoRepository.findByKey(id);
        if (optional.isEmpty()) {
            log.error("Video not found {}" , id);
            throw new ItemNotFoundException("This channel not found!");
        }
        VideoEntity entity = optional.get();


        return videoRepository.save(entity);
    }


//    public Optional<VideoDTO> listById(String cId) {
//        Optional<VideoEntity> optional = videoRepository.findById(cId);
//        if (optional.isEmpty()){
//            log.error("published channel not found {}" , cId);
//            throw new ItemNotFoundException("Not found!");
//        }
//        Optional<VideoEntity> entityList = videoRepository.findByStatusAndVisible(VideoStatus.ACTIVE, true);
//        if (entityList.isEmpty()) {
//            log.error("published channel not found {}" , cId);
//            throw new ItemNotFoundException("Not found!");
//        }
//        return Optional.of(entityToDtoListOptional(entityList));
//    }
//
//    public List<VideoDTO> list(Integer pId) {
//        List<VideoEntity> optional = videoRepository.findByProfile(pId);
//
//        return entityToDtoList(optional);
//    }



//    public void delete(String id) {
//        boolean exist = videoRepository.existsByIdAndVisible(id, true);
//        if (!exist) {
//            log.error(" articleid not found {}" , id);
//            throw new BadRequestException("Not found");
//        }
//        videoRepository.changeStatus(id);
//    }


    public VideoEntity get(String id) {
        return videoRepository.findById(id).orElseThrow(() -> {
            log.error("article  not found {}" , id);
            throw new ItemNotFoundException("Article not found");
        });
    }


    public PageImpl<VideoDTO> pagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<VideoEntity> all = videoRepository.findAll(pageable);

        List<VideoEntity> entityList = all.getContent();
        List<VideoDTO> dtoList = entityToDtoList(entityList);

        return new PageImpl<>(dtoList, pageable, all.getTotalElements());
    }

//    private VideoDTO save(VideoEntity entity, VideoDTO dto, Integer pId) {
//        entity.setName(dto.getName());
//        entity.setKey(dto.getKey());
//        entity.setDescription(dto.getDescription());
//
////        AttachEntity attach = attachService.get(dto.getAttach().getId());
////        entity.setAttach(attach);
////
////        AttachEntity banner = attachService.get(dto.getAttach().getId());
////        entity.setAttach(banner);
//
//        ProfileEntity user = new ProfileEntity();
//        user.setId(pId);
//        entity.setProfile(user);
//        entity.setStatus(VideoStatus.ACTIVE);
//
//        videoRepository.save(entity);
//
//        VideoDTO articleDTO = new VideoDTO();
//        articleDTO.setName(entity.getName());
//        articleDTO.setKey(entity.getKey());
//        articleDTO.setDescription(entity.getDescription());
//        //  articleDTO.setRegionEntity(entity.getRegion());
//        articleDTO.setStatus(entity.getStatus());
//        // articleDTO.setCategoryEntity(entity.getCategory());
//        //  articleDTO.setModerator(entity.getModerator());
////        VideoDTO articleDTO = new VideoDTO();
////        articleDTO.setId(entity.getId());
////        articleDTO.setName(entity.getName());
////        articleDTO.setKey(entity.getKey());
////        articleDTO.setAttach(entity.getAttach());
////        articleDTO.setDescription(entity.getDescription());
////        articleDTO.setCreatedDate(entity.getCreatedDate());
////
////        ProfileEntity moder = entity.getProfile();
////        ProfileDTO profileDTO = new ProfileDTO();
////        profileDTO.setName(moder.getName());
////        profileDTO.setSurname(moder.getSurname());
////        profileDTO.setEmail(moder.getEmail());
////        articleDTO.setProfile(profileDTO);
////
////        AttachEntity attach = entity.getAttach();
////        AttachDTO attachDTO = new AttachDTO();
////        attachDTO.setOriginalName(attach.getOriginalName());
////        attachDTO.setExtension(attach.getExtension());
////        attachDTO.setSize(attach.getSize());
////        attachDTO.setPath(attach.getPath());
////        articleDTO.setAttach(attachDTO);
//
//        return articleDTO;
//    }

    private List<VideoDTO> entityToDtoList(List<VideoEntity> entityList) {
//        List<VideoDTO> list = new LinkedList<>();
//        for (VideoEntity entity : entityList) {
//            VideoDTO dto = new VideoDTO();
//            dto.setName(entity.getName());
//            dto.setDescription(entity.getDescription());
//            dto.setAttach(entity.getAttach().getId());
//            dto.setBanner(entity.getBanner());
//            dto.setWebsiteUrl(entity.getWebsiteUrl());
//            dto.setTelefoneUrl(entity.getTelefoneUrl());
//            dto.setInstagramUrl(entity.getInstagramUrl());
//            dto.setStatus(entity.getStatus());
//            dto.setCreatedDate(entity.getCreatedDate());
//            dto.setVisible(entity.getVisible());
//            dto.setProfile(entity.getProfile());
//            dto.setKey(entity.getKey());
//            list.add(dto);
//        }
        return null;//list;
    }

    private VideoDTO entityToDtoListOptional(Optional<VideoEntity> entityList) {
//        VideoEntity channel = entityList.get();
//
//        VideoDTO dto = new VideoDTO();
//        dto.setName(channel.getName());
//        dto.setDescription(channel.getDescription());
//        dto.setAttach(channel.getAttach().getId());
//        dto.setBanner(channel.getBanner());
//        dto.setWebsiteUrl(channel.getWebsiteUrl());
//        dto.setTelefoneUrl(channel.getTelefoneUrl());
//        dto.setInstagramUrl(channel.getInstagramUrl());
//        dto.setStatus(channel.getStatus());
//        dto.setCreatedDate(channel.getCreatedDate());
//        dto.setVisible(channel.getVisible());
//        dto.setProfile(channel.getProfile());
//        dto.setKey(channel.getKey());

        return null;//dto
    }

    public boolean isExistArticle(String id) {
            return videoRepository.existsById(id);

    }
}
