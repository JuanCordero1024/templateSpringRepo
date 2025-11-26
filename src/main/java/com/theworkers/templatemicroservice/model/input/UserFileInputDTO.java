package com.theworkers.templatemicroservice.model.input;

import com.theworkers.templatemicroservice.model.enums.FileCategory;
import com.theworkers.templatemicroservice.model.enums.FileStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class UserFileInputDTO {

    @Schema
    private UUID userId;

    @Schema
    @NotBlank(message = "File Name can not be blank")
    private String fileName;
    private FileCategory fileCategory;
    private String fileType;
    @Schema
    private FileStatus status;

    @Schema
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Schema
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
