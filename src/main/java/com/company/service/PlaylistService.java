package com.company.service;

import com.company.dto.PlaylistDTO;
import com.company.entity.ChannelEntity;
import com.company.entity.PlaylistEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.attach.AttachEntity;
import com.company.enums.PlaylistStatus;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.PlaylistRepository;
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
public class PlaylistService {
    @Autowired
    private AttachService attachService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private PlaylistRepository playlistRepository;

    public PlaylistDTO create(PlaylistDTO dto, Integer profileId) {
        PlaylistEntity entity = new PlaylistEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        AttachEntity attach = attachService.get(dto.getReview());
        entity.setReview(attach);

        ChannelEntity channel = channelService.get(dto.getChannel());
        entity.setChannel(channel);

        entity.setStatus(dto.getStatus());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setUpdatedDate(null);

        playlistRepository.save(entity);

        PlaylistDTO articleDTO = new PlaylistDTO();
        articleDTO.setName(entity.getName());
        articleDTO.setDescription(entity.getDescription());
        //  articleDTO.setRegionEntity(entity.getRegion());
        articleDTO.setStatus(entity.getStatus());
        // articleDTO.setCategoryEntity(entity.getCategory());
        //  articleDTO.setModerator(entity.getModerator());

        return articleDTO;
    }

    public PlaylistEntity fullUpdate(Integer id, PlaylistDTO dto) {
        Optional<PlaylistEntity> optional = playlistRepository.findById(id);
        if (optional.isEmpty()) {
            log.error("Playlist not found {}" , id);
            throw new ItemNotFoundException("This channel not found!");
        }
        PlaylistEntity entity = optional.get();

        AttachEntity attach = attachService.get(dto.getReview());
        entity.setReview(attach);

        ChannelEntity channel = channelService.get(dto.getChannel());
        entity.setChannel(channel);


        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedDate(LocalDateTime.now());
        return playlistRepository.save(entity);
    }


    public PlaylistEntity changStatus(Integer id) {
        Optional<PlaylistEntity> optional = playlistRepository.findById(id);
        if (optional.isEmpty()) {
            log.error("Playlist not found {}" , id);
            throw new ItemNotFoundException("This channel not found!");
        }

        PlaylistEntity channel = optional.get();
        if (channel.getStatus().equals(PlaylistStatus.PUBLIC)) {
            channel.setStatus(PlaylistStatus.PRIVATE);
        } else {
            channel.setStatus(PlaylistStatus.PUBLIC);
        }

        return playlistRepository.save(channel);
    }


//    public Optional<PlaylistDTO> listById(String cId) {
//        Optional<PlaylistEntity> optional = playlistRepository.findById(cId);
//        if (optional.isEmpty()){
//            log.error("published channel not found {}" , cId);
//            throw new ItemNotFoundException("Not found!");
//        }
//        Optional<PlaylistEntity> entityList = playlistRepository.findByStatusAndVisible(PlaylistStatus.ACTIVE, true);
//        if (entityList.isEmpty()) {
//            log.error("published channel not found {}" , cId);
//            throw new ItemNotFoundException("Not found!");
//        }
//        return Optional.of(entityToDtoListOptional(entityList));
//    }
//
//    public List<PlaylistDTO> list(Integer pId) {
//        List<PlaylistEntity> optional = playlistRepository.findBychannel(pId);
//
//        return entityToDtoList(optional);
//    }



    public void delete(Integer id) {
        boolean exist = playlistRepository.existsByIdAndVisible(id, true);
        if (!exist) {
            log.error(" playlist not found {}" , id);
            throw new BadRequestException("Not found");
        }
        playlistRepository.changeStatus(id);
    }


    public PlaylistEntity get(Integer id) {
        return playlistRepository.findById(id).orElseThrow(() -> {
            log.error("article  not found {}" , id);
            throw new ItemNotFoundException("Article not found");
        });
    }


    public PageImpl<PlaylistDTO> pagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PlaylistEntity> all = playlistRepository.findAll(pageable);

        List<PlaylistEntity> entityList = all.getContent();
        List<PlaylistDTO> dtoList = entityToDtoList(entityList);

        return new PageImpl<>(dtoList, pageable, all.getTotalElements());
    }

    private List<PlaylistDTO> entityToDtoList(List<PlaylistEntity> entityList) {
        List<PlaylistDTO> list = new LinkedList<>();
        for (PlaylistEntity entity : entityList) {
            PlaylistDTO dto = new PlaylistDTO();
            dto.setName(entity.getName());
            dto.setDescription(entity.getDescription());
            dto.setChannel(entity.getChannel().getId());
            dto.setReview(entity.getReview().getId());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setUpdatedDate(entity.getUpdatedDate());
            dto.setStatus(entity.getStatus());
            dto.setVisible(entity.getVisible());
            list.add(dto);
        }
        return list;
    }

    private PlaylistDTO entityToDtoListOptional(Optional<PlaylistEntity> entityList) {
        PlaylistEntity channel = entityList.get();

        PlaylistDTO dto = new PlaylistDTO();
        dto.setName(channel.getName());
        dto.setDescription(channel.getDescription());
        dto.setChannel(channel.getChannel().getId());
        dto.setReview(channel.getReview().getId());
        dto.setCreatedDate(channel.getCreatedDate());
        dto.setUpdatedDate(channel.getUpdatedDate());
        dto.setStatus(channel.getStatus());
        dto.setVisible(channel.getVisible());

        return dto;
    }
}
