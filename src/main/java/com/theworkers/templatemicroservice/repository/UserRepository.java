package com.theworkers.templatemicroservice.repository;

import com.theworkers.templatemicroservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
