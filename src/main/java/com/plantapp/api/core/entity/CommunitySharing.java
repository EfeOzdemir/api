package com.plantapp.api.core.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.plantapp.api.core.model.CSharingDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(
        name = "list-sharing",
        classes = {
                @ConstructorResult(
                        targetClass = CSharingDto.class,
                        columns = {
                                @ColumnResult(name = "id"),
                                @ColumnResult(name = "title"),
                                @ColumnResult(name = "content"),
                                @ColumnResult(name = "imageUrl"),
                                @ColumnResult(name = "createdAt", type = Instant.class),
                                @ColumnResult(name = "likeCount", type = Integer.class),
                                @ColumnResult(name = "commentCount", type = Integer.class),
                                @ColumnResult(name = "isLiked", type = Boolean.class),
                                @ColumnResult(name = "userId"),
                                @ColumnResult(name = "username")
                        }
                )
        }
)
@NamedNativeQuery(
        name = "findAllSharing",
        query = "SELECT cs.id as id, cs.title as title, cs.content as content, cs.image_url as imageUrl, " +
                "cs.created_at as createdAt, " + "(SELECT COUNT(user_id) FROM community_likes l WHERE cs.id = l.community_sharing_id) as likeCount, " +
                "(SELECT COUNT(c.id) FROM comment c WHERE c.community_sharing_id = cs.id) " + "as commentCount, " +
                "IFNULL((SELECT true FROM community_likes WHERE community_sharing_id = cs.id AND user_id = :userId), false) " +
                "as isLiked, " + "u.id as userId, u.username as username " + "FROM community_sharing cs " + "LEFT JOIN users u ON cs.created_by_id = u.id",
        resultSetMapping = "list-sharing"
)
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
    private Set<User> usersWhoLike = new HashSet<>();

    @Size(max = 4)
    @BatchSize(size = 8)
    @OneToMany(mappedBy = "communitySharing")
    private List<Comment> comments = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

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
