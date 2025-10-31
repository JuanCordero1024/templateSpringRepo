package com.theworkers.rolemicroservice.service.impl;

import com.theworkers.rolemicroservice.model.Role;
import com.theworkers.rolemicroservice.model.input.RoleInputDTO;
import com.theworkers.rolemicroservice.model.mappers.RoleMapper;
import com.theworkers.rolemicroservice.model.output.RoleOutputDTO;
import com.theworkers.rolemicroservice.model.output.WebResponse;
import com.theworkers.rolemicroservice.repository.RoleRepository;
import com.theworkers.rolemicroservice.service.RoleService;
import com.theworkers.rolemicroservice.util.ValidateWebException;
import com.theworkers.rolemicroservice.util.validators.RoleValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleValidator roleValidator;

    public RoleServiceImpl(RoleRepository roleRepository,
                           RoleMapper roleMapper,
                           RoleValidator roleValidator) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.roleValidator = roleValidator;
    }

    public WebResponse createRole(RoleInputDTO newRole) {
        try{
            roleValidator.RoleValidator(newRole);
            roleValidator.validateIfExistNameRole(newRole.getName(),roleRepository);

            Role newRoleEntity = roleMapper.toEntity(newRole);
            roleRepository.save(newRoleEntity);

            return new WebResponse("Successfully registered", HttpStatus.CREATED,null);

        }catch(ValidateWebException e){throw e;}
    }

    public WebResponse readRole(String roleName) {
        try{
            roleValidator.validateIfExistByNameRole(roleName, roleRepository);
            return new WebResponse("Successfully read role", HttpStatus.OK,
                                    roleMapper.toOutputEntity(roleRepository.getRoleByName(roleName)));
        }catch (ValidateWebException e){throw e;}
    }

    public WebResponse udpateRole(RoleInputDTO user) {
        return null;
    }

    public WebResponse deleteRole(Long roleId) {
        return null;
    }
}
