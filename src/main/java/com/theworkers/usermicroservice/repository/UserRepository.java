package com.theworkers.usermicroservice.repository;

import com.theworkers.usermicroservice.model.User;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    @NotEmpty(message = "The email can not be null") User getUserByEmail(String email);

    User getUserById(UUID id);
}
