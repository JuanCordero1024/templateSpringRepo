package com.theworkers.usermicroservice.repository;

import com.theworkers.usermicroservice.model.User;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @NotEmpty(message = "The password can not be null") User getUserByEmail(String email);
}
