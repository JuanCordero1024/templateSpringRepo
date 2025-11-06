package com.theworkers.usermicroservice.model.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Is the body that you need to send for one request to the backend for Roles")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleOutputDTO {
    @Schema
    private Long id;
    @Schema
    private String name;
    @Schema
    private String description;
}
