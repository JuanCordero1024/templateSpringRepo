package com.theworkers.usermicroservice.model.input;

import com.theworkers.usermicroservice.model.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Data
@Schema(description = "Body to send and create one User")
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

    @Schema(description = "User's employee number")
    @NotBlank(message = "The employee number is required.")
    private String employeeNumber;

    @Schema(description = "User status: ACTIVO, INACTIVO, BAJA_TEMPORAL")
    private UserStatus status;

    @Schema(description = "User's password")
    private String password;

    @Schema(description = "User's ID role")
    private Long role;

}
