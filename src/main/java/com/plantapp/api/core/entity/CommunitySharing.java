package com.plantapp.api.core.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CommunitySharing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedBy
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonManagedReference
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
    @JsonBackReference
    private Set<User> usersWhoLike;

    @OneToMany(mappedBy = "communitySharing")
    private List<Comment> comments;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Transient
    public Boolean getIsLiked() {
        return false;
    }

    @Transient
    public Integer getLikeCount() {
        return 0;
    }

    public void likeBy(User user) {
        this.usersWhoLike.add(user);
        user.getLikes().add(this);
    }

    public void unlikeBy(User user) {
        this.usersWhoLike.remove(user);
        user.getLikes().remove(this);
    }

    public boolean isLikedBy(User user) {
        return usersWhoLike.contains(user);
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
