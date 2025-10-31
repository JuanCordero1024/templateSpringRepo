package com.theworkers.rolemicroservice.model.output;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Is the body that you need to send for one request to the backend for Roles")
public class RoleOutputDTO {
    @Schema
    private Long id;
    @Schema
    private String name;
    @Schema
    private String description;
}
