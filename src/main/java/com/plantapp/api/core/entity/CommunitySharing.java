package com.plantapp.api.core.entity;

import jakarta.persistence.*;
import lombok.Builder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;

@Entity
@Builder
public class CommunitySharing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMUNITY_SHARING_ID_SEQUENCE")
    @SequenceGenerator(name = "COMMUNITY_SHARING_ID_SEQUENCE", sequenceName = "COMMUNITY_SHARING_ID_SEQUENCE", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedBy
    private String createdBy;

    @Column(nullable = false)
    private String imageUrl;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
