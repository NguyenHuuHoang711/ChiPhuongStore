package com.my_shop.authservice.src.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.my_shop.authservice.src.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    @JsonIgnore
    private String password;

    private USER_ROLE role;

    @PrePersist
    public void generateId() {
        if (this.id == null || this.id.isBlank()) {
            this.id = com.my_shop.authservice.src.util.IdGenerator.GenerateId();
        }
    }
}
