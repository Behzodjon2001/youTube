package com.company.dto.video;

import com.company.entity.TagEntity;
import com.company.entity.video.VideoEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoTagDTO {
    private Integer id;

    private VideoEntity video;

    private TagEntity tag;

    private LocalDateTime createdDate;
}
