package com.company.entity;

import com.company.entity.attach.AttachEntity;
import com.company.enums.ChannelStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "channel")
public class ChannelEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id")
    private AttachEntity attach;

    @Column()
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private ChannelStatus status=ChannelStatus.ACTIVE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private AttachEntity banner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

    @Column
    private String websiteUrl;
    @Column
    private String telefoneUrl;
    @Column
    private String instagramUrl;

    @Column(nullable = false, unique = true)
    private String key;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
