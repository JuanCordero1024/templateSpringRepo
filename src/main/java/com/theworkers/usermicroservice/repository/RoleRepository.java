package com.theworkers.usermicroservice.repository;

import com.theworkers.usermicroservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
