package com.theworkers.templatemicroservice.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(description = "This is the parameters that you need to implement into frondend")
public class UserLoginInput {
    @Schema(description = "User's email")
    @Email(message = "The email format is invalid.")
    @NotEmpty(message = "The email can not be empty")
    private String email;

    @Schema(description = "User's password")
    @NotEmpty(message = "The password can not be null")
    private String password;

    @Schema(description = "User's Ecc Public Token")
    @NotEmpty(message = "The ECC public token can not be null")
    private String eccToken;
}
