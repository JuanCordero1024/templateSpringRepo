package com.theworkers.templatemicroservice.repository;

import com.theworkers.templatemicroservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
