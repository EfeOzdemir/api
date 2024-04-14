package com.plantapp.api.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CommunitySharing communitySharing;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;
}
