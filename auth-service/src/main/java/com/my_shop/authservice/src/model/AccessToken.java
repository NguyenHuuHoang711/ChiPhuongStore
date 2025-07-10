package com.my_shop.authservice.src.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "access_token")
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private String userId;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private boolean revoked;

    @PrePersist
    public void generateId() {
        if (this.id == null || this.id.isBlank()) {
            this.id = com.my_shop.authservice.src.util.IdGenerator.GenerateId();
        }
    }
}

