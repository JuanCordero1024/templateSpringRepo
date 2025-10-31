package com.theworkers.rolemicroservice.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(description = "Body that you need to send one request to our API")
public class RoleInputDTO {
    @Schema(description = "User's name")
    @NotBlank(message = "The name cannot be blank.")
    private String name;

    @Schema(description = "Description of the Role")
    private String descritpion;

}
