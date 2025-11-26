package com.theworkers.templatemicroservice.repository;

import com.theworkers.templatemicroservice.model.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface UserFileRepository extends JpaRepository<UserFile, UUID>, JpaSpecificationExecutor<UserFile> {
    UserFile getUserFileById(UUID Id);

    List<UserFile> getUserFilesByUserId(UUID userId);
}
