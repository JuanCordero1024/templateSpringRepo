package com.theworkers.usermicroservice.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RoleInputDTO {
    @Schema(description = "User's name")
    @NotBlank(message = "The name cannot be blank.")
    private String name;

    @Schema(description = "User's email")
    private String descritpion;

}
