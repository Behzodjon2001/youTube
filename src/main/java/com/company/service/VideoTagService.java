package com.company.service;

import com.company.dto.video.VideoTagDTO;
import com.company.entity.video.VideoTagEntity;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.VideoTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VideoTagService {
    @Autowired
    private VideoTagRepository videoTagRepository;

    public VideoTagDTO create(VideoTagDTO dto) {
//        Optional<VideoTagEntity> optional = videoTagRepository.findByName(dto.getName());
//        if (optional.isPresent()) {
//            log.error("This category already exist {}" , dto);
//            throw new BadRequestException("This category already exist");
//        }
//        VideoTagEntity entity = new VideoTagEntity();
//        entity.setName(dto.getName());
//        videoTagRepository.save(entity);
//
//        dto.setCreatedDate(entity.getCreated_Date());
//        dto.setId(entity.getId());
        return dto;
    }

    public List<VideoTagDTO> list() {
        Iterable<VideoTagEntity> all = videoTagRepository.findAll();
        List<VideoTagDTO> list = new LinkedList<>();
        for (VideoTagEntity entity : all) {
            VideoTagDTO dto = new VideoTagDTO();
            dto.setId(entity.getId());
//            dto.setName(entity.getName());
//
//            dto.setCreatedDate(entity.getCreated_Date());

            list.add(dto);
        }
        return list;
    }

    public void update(VideoTagDTO dto,Integer id) {
        VideoTagEntity entity = get(id);
//        entity.setName(dto.getName());
        videoTagRepository.save(entity);
    }

    public VideoTagEntity get(Integer id) {
        return videoTagRepository.findById(id).orElseThrow(() -> {
            log.error("This category not found {}" , id);
            throw new ItemNotFoundException("This category not found");
        });
    }

    public void delete(Integer id) {
        Optional<VideoTagEntity> optional = videoTagRepository.findById(id);
        if (optional.isEmpty()) {
            log.error("This category not found {}" , id);
            throw new BadRequestException("This category not found");
        }
        videoTagRepository.deleteById(id);
    }
}
