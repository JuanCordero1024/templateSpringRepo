package com.theworkers.templatemicroservice.model.input;

import com.theworkers.templatemicroservice.model.enums.FileStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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
public class UserFileSignInputDTO {
    @Schema
    private UUID documentId;

    @Schema
    private UUID userId;
}
