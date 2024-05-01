package com.plantapp.api.core.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.plantapp.api.core.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    private String id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 25)
    private String city;

    @Column(length = 75)
    private String occupation;

    @OneToMany(mappedBy = "createdBy")
    @JsonBackReference
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(mappedBy = "usersWhoLike")
    @JsonBackReference
    private List<Post> likes;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(this.id, user.id);
    }
}
