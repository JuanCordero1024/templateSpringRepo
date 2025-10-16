package com.theworkers.templatemicroservice.util.validators;

import com.theworkers.templatemicroservice.model.input.UserInputDTO;
import com.theworkers.templatemicroservice.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.theworkers.templatemicroservice.util.ValidateWebException;

@Component
public class UserValidator {

    public static void UserValidator(UserInputDTO userInputDTO) {
        GeneralValidator.notNullValidator.validate(userInputDTO.getEmail(), "Email");
        GeneralValidator.notNullValidator.validate(userInputDTO.getName(), "Name");
        GeneralValidator.notNullValidator.validate(userInputDTO.getLastName(), "Last Name");
        GeneralValidator.notNullValidator.validate(userInputDTO.getMidleName(), "Middle Name");
        GeneralValidator.notNullValidator.validate(userInputDTO.getPassword(), "Password");
        GeneralValidator.notNullValidator.validate(userInputDTO.getRole(), "Role");
    }

    public static void validateUserRole(Long roleId, RoleRepository roleRepository) {
        if(!roleRepository.findById(roleId).isPresent()){
            throw new ValidateWebException("That role doesn't exist", HttpStatus.BAD_REQUEST);
        }
    }
}
