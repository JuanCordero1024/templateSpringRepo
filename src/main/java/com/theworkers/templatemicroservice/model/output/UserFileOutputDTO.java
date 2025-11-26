package com.theworkers.templatemicroservice.model.output;

import com.theworkers.templatemicroservice.model.enums.FileStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserFileOutputDTO {

    @Schema
    private UUID id;
    @Schema
    private UUID userId;
    @Schema
    private String fileName;
    @Schema
    private String fileCategory;
    @Schema
    private FileStatus status;

    @Schema
    private String originalFileHash;
    @Schema
    private String signedFileHash;

    @Schema
    private String digitalSignature;

    @Schema
    private LocalDateTime createdAt;
    @Schema
    private LocalDateTime updatedAt;

}
