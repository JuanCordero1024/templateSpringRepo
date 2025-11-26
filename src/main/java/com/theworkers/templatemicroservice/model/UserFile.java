package com.theworkers.templatemicroservice.model;

import com.theworkers.templatemicroservice.model.enums.FileCategory;
import com.theworkers.templatemicroservice.model.enums.FileStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_files")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(nullable = false)
    private UUID userId;

    private String fileName;
    @Enumerated(EnumType.STRING)
    private FileCategory fileCategory;
    private String fileType;

    @Column(nullable = false)
    private String originalFilePath;

    private String signedFilePath;

    @Column(name = "original_file_hash", length = 64)
    private String originalFileHash;

    @Column(name = "signed_file_hash", length = 64)
    private String signedFileHash;

    @Column(columnDefinition = "TEXT")
    private String digitalSignature;

    @Enumerated(EnumType.STRING)
    private FileStatus status;

    @Lob
    private byte[] originalFile;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
