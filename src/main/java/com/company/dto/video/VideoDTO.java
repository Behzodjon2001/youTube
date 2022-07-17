package com.company.dto.video;

import com.company.entity.CategoryEntity;
import com.company.entity.ChannelEntity;
import com.company.entity.attach.AttachEntity;
import com.company.enums.VideoStatus;
import com.company.enums.VideoType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class VideoDTO {
    private String uuid;

    private String name;

    private String title;

    private String description;

    private String attachId;

    private AttachEntity attach;

    private String reviewId;

    private AttachEntity review;

    private String channelId;

    private ChannelEntity channel;

    private Integer categoryId;

    private CategoryEntity category;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime publishedDate;

    private Integer time;

    private VideoType type;

    private Integer sharedCount = 0;

    private Boolean visible = Boolean.TRUE;

    private VideoStatus status = VideoStatus.PUBLIC;

    private String key;
}
