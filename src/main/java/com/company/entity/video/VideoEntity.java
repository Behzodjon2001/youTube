package com.company.entity.video;

import com.company.entity.CategoryEntity;
import com.company.entity.ChannelEntity;
import com.company.entity.attach.AttachEntity;
import com.company.enums.PlaylistStatus;
import com.company.enums.VideoStatus;
import com.company.enums.VideoType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "video")
public class VideoEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false, unique = true)
    private String key;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preview_attach_id")
    private AttachEntity review;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @JoinColumn(name = "attach_id")
    @OneToOne(fetch = FetchType.LAZY)
    private AttachEntity attach;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column
    @Enumerated(EnumType.STRING)
    private VideoStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private VideoType type;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "shared_count")
    private Integer sharedCount = 0;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private ChannelEntity channel;

    public VideoEntity(String id) {
        this.id = id;
    }
}
