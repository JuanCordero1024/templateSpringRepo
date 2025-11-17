package com.theworkers.usermicroservice.model.output;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class UserOutputDTO {

    @Schema(description = "Id of user Output")
    private UUID id;

    @Schema(description = "User's last name")
    private String lastName;

    @Schema(description = "User's middle name")
    private String middleName;

    @Schema(description = "User's email")
    private String email;

    @Schema(description = "User's role ID")
    private Long roleId;

    @Schema(description = "User's email")
    private String token;


}