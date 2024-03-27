package com.plantapp.api.core.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@Builder
public class CommunitySharing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMUNITY_SHARING_ID_SEQUENCE")
    @SequenceGenerator(name = "COMMUNITY_SHARING_ID_SEQUENCE", sequenceName = "COMMUNITY_SHARING_ID_SEQUENCE", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedBy
    @ManyToOne(optional = false)
    private User createdBy;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "community_likes",
            joinColumns = @JoinColumn(name = "community_sharing_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedBy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public void likeBy(User user) {
        this.likedBy.add(user);
        user.getLikes().add(this);
    }

    public void unlikeBy(User user) {
        this.likedBy.remove(user);
        user.getLikes().remove(this);
    }

    public boolean isLikedBy(User user) {
        return likedBy.contains(user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        CommunitySharing communitySharing = (CommunitySharing) obj;
        return Objects.equals(this.id, communitySharing.id);
    }
}
