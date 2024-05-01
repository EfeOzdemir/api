package com.plantapp.api.core.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.plantapp.api.core.model.dto.PostDto;
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
                name = "list-posts",
                classes = {
                        @ConstructorResult(
                                targetClass = PostDto.class,
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
        @SqlResultSetMapping(name = "list-posts.count", columns = @ColumnResult(name = "count"))
})
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "findAllPosts",
                query = "SELECT " +
                        "p.id AS id, " +
                        "p.title AS title, " +
                        "CASE " +
                        "   WHEN CHAR_LENGTH(p.content) > 50 THEN CONCAT(TRIM(SUBSTRING(p.content, 1, 50)), '...') " +
                        "   ELSE p.content " +
                        "END AS content, " +
                        "p.image_url AS imageUrl, " +
                        "p.created_at AS createdAt, " +
                        "( " +
                        "   SELECT COUNT(user_id) " +
                        "   FROM post_likes l " +
                        "   WHERE p.id = l.post_id " +
                        ") AS likeCount, " +
                        "( " +
                        "   SELECT COUNT(c.id) " +
                        "   FROM comment c " +
                        "   WHERE c.post_id = p.id " +
                        ") AS commentCount, " +
                        "CASE " +
                        "   WHEN :userId IS NOT NULL THEN IFNULL(( " +
                        "       SELECT TRUE " +
                        "       FROM post_likes " +
                        "       WHERE post_id = p.id AND user_id = :userId " +
                        "   ), FALSE) " +
                        "   ELSE FALSE " +
                        "END AS isLiked, " +
                        "u.id AS userId, " +
                        "u.username AS username " +
                        "FROM post p " +
                        "LEFT JOIN user u ON p.created_by_id = u.id",
                resultSetMapping = "list-posts"
        ),
        @NamedNativeQuery(
                name = "findPostById",
                query = "SELECT " +
                        "p.id AS id, " +
                        "p.title AS title, " +
                        "p.content AS content, " +
                        "p.image_url AS imageUrl, " +
                        "p.created_at AS createdAt, " +
                        "( " +
                        "   SELECT COUNT(user_id) " +
                        "   FROM post_likes l " +
                        "   WHERE p.id = l.post_id " +
                        ") AS likeCount, " +
                        "( " +
                        "   SELECT COUNT(c.id) " +
                        "   FROM comment c " +
                        "   WHERE c.post_id = p.id " +
                        ") AS commentCount, " +
                        "CASE " +
                        "   WHEN :userId IS NOT NULL THEN IFNULL(( " +
                        "       SELECT TRUE " +
                        "       FROM post_likes " +
                        "       WHERE post_id = p.id AND user_id = :userId " +
                        "   ), FALSE) " +
                        "   ELSE FALSE " +
                        "END AS isLiked, " +
                        "u.id AS userId, " +
                        "u.username AS username " +
                        "FROM post p " +
                        "LEFT JOIN user u ON p.created_by_id = u.id " +
                        "WHERE p.id = :postId",
                resultSetMapping = "list-post"
        ),
        @NamedNativeQuery(
                name = "findAllPosts.count",
                query = "SELECT COUNT(*) AS count FROM post",
                resultSetMapping = "list-posts.count"
        )
})

@EntityListeners(AuditingEntityListener.class)
public class Post {

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
    @JoinTable(name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    private Set<User> usersWhoLike = new HashSet<>();

    @Size(max = 4)
    @BatchSize(size = 8)
    @OneToMany(mappedBy = "post")
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
        Post post = (Post) obj;
        return Objects.equals(this.id, post.id);
    }
}
