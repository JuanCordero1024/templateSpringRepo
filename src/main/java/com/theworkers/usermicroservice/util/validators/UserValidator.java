package com.theworkers.usermicroservice.util.validators;

import com.theworkers.usermicroservice.model.User;
import com.theworkers.usermicroservice.model.input.UserInputDTO;
import com.theworkers.usermicroservice.model.input.UserLoginInput;
import com.theworkers.usermicroservice.model.output.WebResponse;
import com.theworkers.usermicroservice.repository.RoleRepository;
import com.theworkers.usermicroservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public static boolean validateUserByEmail(UserLoginInput userLoginInput, UserRepository userRepository) {
        if(userLoginInput.getEmail().equals(userRepository.getUserByEmail(userLoginInput.getEmail()))) {
            return true;
        }
        return false;
    }

    public static boolean validateUserCrendentials(UserLoginInput credentials, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        validateUserByEmail(credentials, userRepository);
        if(!passwordEncoder.matches(credentials.getPassword(), userRepository.getUserByEmail(credentials.getEmail()).getPassword())) {
            return false;
        }
        return true;
    }

}
