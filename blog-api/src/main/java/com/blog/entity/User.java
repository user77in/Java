package com.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.management.relation.Role;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(length = 100)
    private String displayName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.READER;

    @Builder.Default
    private Boolean isActive = true;

    public enum Role {
        READER, AUTHOR, ADMIN
    }

}
























