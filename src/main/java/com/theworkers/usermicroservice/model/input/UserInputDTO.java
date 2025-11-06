package com.theworkers.usermicroservice.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserInputDTO {

    @Schema(description = "User's name")
    private String name;

    @Schema(description = "User's last name")
    private String lastName;

    @Schema(description = "User's midle name")
    private String middleName;

    @Schema(description = "User's email")
    @Email(message = "The email format is invalid.")
    private String email;

    @Schema(description = "User's password")
    private String password;

    @Schema(description = "User's ID role")
    private Long role;

}
