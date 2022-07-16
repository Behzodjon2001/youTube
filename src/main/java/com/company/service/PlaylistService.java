package com.company.service;

import com.company.dto.PlaylistDTO;
import com.company.dto.VideoShortInfoDTO;
import com.company.dto.playlist.PlaylistFullDTO;
import com.company.dto.playlist.PlaylistShortInfoDTO;
import com.company.entity.ChannelEntity;
import com.company.entity.PlaylistEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.attach.AttachEntity;
import com.company.enums.PlaylistStatus;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.mapper.PlaylistFullInfo;
import com.company.mapper.PlaylistShortInfo;
import com.company.mapper.PlaylistVideoLimit2;
import com.company.repository.ChannelRepository;
import com.company.repository.PlaylistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.nio.channels.Channel;
import java.time.LocalDateTime;
import java.util.*;

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
        entity.setOrderNum(dto.getOrderNum());

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
        entity.setOrderNum(dto.getOrderNum());
        entity.setUpdatedDate(LocalDateTime.now());
        return playlistRepository.save(entity);
    }

//    public List<PlaylistDTO> getProfilePlayList() {
//        ProfileEntity entity = profileService.getProfile();
//        List<PlaylistDTO> playlistService = new LinkedList<>();
//
//        List<PlaylistShortInfo> shortInfolist = playlistRepository.getPlayLists(entity.getId());
//        for (PlaylistShortInfo info: shortInfolist){
//            PlaylistDTO dto = new PlaylistDTO();
//            dto.setId(info.getPlaylistId());
//            dto.setName(info.getPlaylistName());
//            dto.setCreatedDate(info.getPlaylistCreationDate());
//            dto.setChannel(new Channel(info.getPlaylist));
//        }
//    }


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


//    public PlaylistDTO listById(Integer cId) {
//        Optional<PlaylistEntity> optional = playlistRepository.findById(cId);
//        if (optional.isEmpty()){
//            log.error("published channel not found {}" , cId);
//            throw new ItemNotFoundException("Not found!");
//        }
//        Optional<PlaylistEntity> entityList = playlistRepository.findByVisible(true);
//        if (entityList.isEmpty()) {
//            log.error("published channel not found {}" , cId);
//            throw new ItemNotFoundException("Not found!");
//        }
//        return entityToDtoListShort(entityList);
//    }

//    public PlaylistDTO listCurrentUser(Integer cId) {
//        List<ChannelEntity> optionalProf = channelRepository.findByProfile(cId);
//        if (optionalProf.isEmpty()){
//            log.error("published channel not found {}" , cId);
//            throw new ItemNotFoundException("Not found!");
//        }
//
//
//        Optional<PlaylistEntity> optional = playlistRepository.findById(optionalProf.get(cId).getProfile().getId());
//        if (optional.isEmpty()){
//            log.error("published channel not found {}" , cId);
//            throw new ItemNotFoundException("Not found!");
//        }
//        Optional<PlaylistEntity> entityList = playlistRepository.findByVisible(true);
//        if (entityList.isEmpty()) {
//            log.error("published channel not found {}" , cId);
//            throw new ItemNotFoundException("Not found!");
//        }
//        return entityToDtoListShort(entityList);
//    }

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

    public List<PlaylistShortInfoDTO> getPlaylistByChannel(String channelId) {

        List<PlaylistShortInfoDTO> playlistShortInfoDTOS = new ArrayList<>();

        playlistRepository.getPlayListByChannelId(channelId).forEach(playlistShortInfo -> {

            List<PlaylistVideoLimit2> infoLimit2 = playlistRepository
                    .playlistShortInfoLimit2(playlistShortInfo.getPlaylistId());

            PlaylistShortInfoDTO dto = new PlaylistShortInfoDTO();
            dto.setVideoShortInfoDTOS(infoLimit2);
            dto.setPlaylistId(playlistShortInfo.getPlaylistId());
            dto.setChannelId(playlistShortInfo.getChannelId());
            dto.setChannelName(playlistShortInfo.getChannelName());
            dto.setPlaylistName(playlistShortInfo.getPlaylistName());
            dto.setPlaylistCreatedDate(playlistShortInfo.getPlaylistCreatedDate());
            dto.setCountVideo(playlistShortInfo.getCountVideo());
//            dto.setAttachUrl(attachService.getAttachOpenUrl());

            playlistShortInfoDTOS.add(dto);
        });

        return playlistShortInfoDTOS;
    }

    public PlaylistFullDTO getPlaylistVideosByPlaylistId(String playlistId) {

        List<PlaylistFullInfo> list = playlistRepository.playlistFullInfoList(playlistId);

        PlaylistFullDTO dto = new PlaylistFullDTO();
        dto.setPlaylistId(list.get(0).getPlaylistId());
        dto.setPlaylistName(list.get(0).getPlaylistName());

        int playlistViewCount = 0;

        List<VideoShortInfoDTO> dtos = new ArrayList<>();

        for (PlaylistFullInfo playlistFullInfo : list) {
            dtos.add(new VideoShortInfoDTO(playlistFullInfo.getVideoId(), playlistFullInfo.getVideoName(),
                    attachService.getAttachOpenUrl(playlistFullInfo.getReviewId()), playlistFullInfo.getViewCount()));

            playlistViewCount += playlistFullInfo.getViewCount();
        }

        dto.setPlaylistViewCount(playlistViewCount);
        dto.setVideoShortInfoDTOS(dtos);

        return dto;
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

    private PlaylistDTO entityToDtoListShort(Optional<PlaylistEntity> entityList) {
        PlaylistEntity channel = entityList.get();

        PlaylistDTO dto = new PlaylistDTO();
        dto.setName(channel.getName());
        dto.setDescription(channel.getDescription());
        dto.setReview(channel.getReview().getId());
        dto.setCreatedDate(channel.getCreatedDate());
        dto.setUpdatedDate(channel.getUpdatedDate());
        dto.setStatus(channel.getStatus());

        return dto;
    }
}
