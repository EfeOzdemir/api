package com.plantapp.api.core.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.plantapp.api.core.model.dto.CSharingDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@SqlResultSetMappings({
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
        ),
        @SqlResultSetMapping(name = "list-sharing.count", columns = @ColumnResult(name = "count"))
})
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "findAllSharing",
                query = "SELECT " +
                        "cs.id AS id, " +
                        "cs.title AS title, " +
                        "CASE " +
                        "   WHEN CHAR_LENGTH(cs.content) > 50 THEN CONCAT(TRIM(SUBSTRING(cs.content, 1, 50)), '...') " +
                        "   ELSE cs.content " +
                        "END AS content, " +
                        "cs.image_url AS imageUrl, " +
                        "cs.created_at AS createdAt, " +
                        "( " +
                        "   SELECT COUNT(user_id) " +
                        "   FROM community_likes l " +
                        "   WHERE cs.id = l.community_sharing_id " +
                        ") AS likeCount, " +
                        "( " +
                        "   SELECT COUNT(c.id) " +
                        "   FROM comment c " +
                        "   WHERE c.community_sharing_id = cs.id " +
                        ") AS commentCount, " +
                        "CASE " +
                        "   WHEN :userId IS NOT NULL THEN IFNULL(( " +
                        "       SELECT TRUE " +
                        "       FROM community_likes " +
                        "       WHERE community_sharing_id = cs.id AND user_id = :userId " +
                        "   ), FALSE) " +
                        "   ELSE FALSE " +
                        "END AS isLiked, " +
                        "u.id AS userId, " +
                        "u.username AS username " +
                        "FROM community_sharing cs " +
                        "LEFT JOIN users u ON cs.created_by_id = u.id",
                resultSetMapping = "list-sharing"
        ),
        @NamedNativeQuery(
                name = "findSharingById",
                query = "SELECT " +
                        "cs.id AS id, " +
                        "cs.title AS title, " +
                        "cs.content AS content, " +
                        "cs.image_url AS imageUrl, " +
                        "cs.created_at AS createdAt, " +
                        "( " +
                        "   SELECT COUNT(user_id) " +
                        "   FROM community_likes l " +
                        "   WHERE cs.id = l.community_sharing_id " +
                        ") AS likeCount, " +
                        "( " +
                        "   SELECT COUNT(c.id) " +
                        "   FROM comment c " +
                        "   WHERE c.community_sharing_id = cs.id " +
                        ") AS commentCount, " +
                        "CASE " +
                        "   WHEN :userId IS NOT NULL THEN IFNULL(( " +
                        "       SELECT TRUE " +
                        "       FROM community_likes " +
                        "       WHERE community_sharing_id = cs.id AND user_id = :userId " +
                        "   ), FALSE) " +
                        "   ELSE FALSE " +
                        "END AS isLiked, " +
                        "u.id AS userId, " +
                        "u.username AS username " +
                        "FROM community_sharing cs " +
                        "LEFT JOIN users u ON cs.created_by_id = u.id " +
                        "WHERE cs.id = :postId",
                resultSetMapping = "list-sharing"
        ),
        @NamedNativeQuery(
                name = "findAllSharing.count",
                query = "SELECT COUNT(*) AS count FROM community_sharing",
                resultSetMapping = "list-sharing.count"
        )
})

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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CommunitySharing communitySharing = (CommunitySharing) obj;
        return Objects.equals(this.id, communitySharing.id);
    }
}
