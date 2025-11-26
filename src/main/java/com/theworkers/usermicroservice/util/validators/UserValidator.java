package com.theworkers.usermicroservice.util.validators;

import com.theworkers.usermicroservice.model.input.UserInputDTO;
import com.theworkers.usermicroservice.model.input.UserLoginInput;
import com.theworkers.usermicroservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.theworkers.usermicroservice.util.ValidateWebException;

import java.util.UUID;

@Component
public class UserValidator {

    public static void UserValidator(UserInputDTO userInputDTO) {
        GeneralValidator.notNullValidator.validate(userInputDTO.getEmail(), "Email");
        GeneralValidator.notNullValidator.validate(userInputDTO.getName(), "Name");
        GeneralValidator.notNullValidator.validate(userInputDTO.getLastName(), "Last Name");
        GeneralValidator.notNullValidator.validate(userInputDTO.getMiddleName(), "Middle Name");
        GeneralValidator.notNullValidator.validate(userInputDTO.getPassword(), "Password");
        GeneralValidator.notNullValidator.validate(userInputDTO.getStatus(), "Status");
        GeneralValidator.notNullValidator.validate(userInputDTO.getRole(), "Role");
    }
    public static void UserLoginValidator(UserLoginInput userInputDTO) {
        GeneralValidator.notNullValidator.validate(userInputDTO.getEmail(), "Email");
        GeneralValidator.notNullValidator.validate(userInputDTO.getPassword(), "Password");
    }

    public static boolean validateUserByUUID(UUID userId, UserRepository userRepository) {
        if(!userRepository.findById(userId).isPresent()) return false;
        return true;
    }

    public static void validateUserByUUIDforExternalClient(UUID userId, UserRepository userRepository) {
        if(!userRepository.findById(userId).isPresent())
            throw new ValidateWebException("User not found", HttpStatus.NOT_FOUND);
    }

    public static void validateUserByEmail(UserLoginInput userLoginInput, UserRepository userRepository) {
        if(!userLoginInput.getEmail().equals(
                userRepository.getUserByEmail(userLoginInput.getEmail()).getEmail()
        ))
            throw new ValidateWebException("Email not found", HttpStatus.CONFLICT);
    }

    public static boolean validateUserCrendentials(UserLoginInput credentials, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        validateUserByEmail(credentials, userRepository);
        if(!passwordEncoder.matches(credentials.getPassword(),
                userRepository.getUserByEmail(credentials.getEmail()).getPassword())) {
            return false;
        }
        return true;
    }


    public static void validateUserIfHaveECCToken(UUID userId, UserRepository userRepository) {
        if (userRepository.getUserById(userId).getEccPublicKey() != null)
            throw new ValidateWebException("User already have ECC Key", HttpStatus.BAD_REQUEST);
    }

}
