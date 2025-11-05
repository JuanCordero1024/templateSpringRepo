package com.theworkers.usermicroservice.repository;

import com.theworkers.usermicroservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
