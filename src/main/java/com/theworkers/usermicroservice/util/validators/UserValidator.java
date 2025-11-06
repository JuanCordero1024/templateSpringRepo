package com.theworkers.usermicroservice.util.validators;

import com.theworkers.usermicroservice.model.input.UserInputDTO;
import com.theworkers.usermicroservice.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.theworkers.usermicroservice.util.ValidateWebException;

@Component
public class UserValidator {

    public static void UserValidator(UserInputDTO userInputDTO) {
        GeneralValidator.notNullValidator.validate(userInputDTO.getEmail(), "Email");
        GeneralValidator.notNullValidator.validate(userInputDTO.getName(), "Name");
        GeneralValidator.notNullValidator.validate(userInputDTO.getLastName(), "Last Name");
        GeneralValidator.notNullValidator.validate(userInputDTO.getMiddleName(), "Middle Name");
        GeneralValidator.notNullValidator.validate(userInputDTO.getPassword(), "Password");
        GeneralValidator.notNullValidator.validate(userInputDTO.getRole(), "Role");
    }

}
