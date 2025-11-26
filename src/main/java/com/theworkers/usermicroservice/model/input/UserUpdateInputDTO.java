package com.theworkers.usermicroservice.model.input;

import com.theworkers.usermicroservice.model.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Data
@Schema(description = "Body to send and create one User")
public class UserUpdateInputDTO {

    @Schema(description = "User's password")
    private String password;

    @Schema(description = "User's ID role")
    private Long role;

}
