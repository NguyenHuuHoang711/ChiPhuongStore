package com.my_shop.authservice.src.repository;

import com.my_shop.authservice.src.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    @NonNull
    Optional<User> findById(@NonNull String id);
}