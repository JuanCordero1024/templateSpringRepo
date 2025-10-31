package com.theworkers.rolemicroservice.util.validators;

import com.theworkers.rolemicroservice.model.input.RoleInputDTO;
import com.theworkers.rolemicroservice.repository.RoleRepository;
import com.theworkers.rolemicroservice.util.ValidateWebException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {

    public static void RoleValidator(RoleInputDTO newRole) {
        GeneralValidator.notNullValidator.validate(newRole.getName(), "Role Name");
    }

    public static void validateIfExistNameRole(String roleName, RoleRepository roleRepository) {
        if(roleRepository.existsByName(roleName)){
            throw new ValidateWebException("Role Name Already Exists", HttpStatus.BAD_REQUEST);
        }
    }

    public static void validateIfExistByNameRole(String roleName, RoleRepository roleRepository) {
        if(!roleRepository.existsByName(roleName)){
            throw new ValidateWebException("Role doesn't Exists", HttpStatus.BAD_REQUEST);
        }
    }

}
