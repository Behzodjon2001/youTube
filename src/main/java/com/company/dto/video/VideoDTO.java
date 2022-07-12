package com.company.dto.video;

import com.company.enums.VideoStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoDTO {
    private String id;

    private String key;

    private String review;

    private String title;

    private String category;

    private String attach;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime publishedDate;

    private VideoStatus status;

    private VideoStatus type;

    private Integer viewCount = 0;

    private Integer sharedCount = 0;

    private String description;

    private String channel;
}
