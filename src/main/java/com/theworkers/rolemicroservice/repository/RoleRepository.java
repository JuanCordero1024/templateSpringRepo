package com.theworkers.rolemicroservice.repository;

import com.theworkers.rolemicroservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);

    Role getRoleByName(String name);
}
