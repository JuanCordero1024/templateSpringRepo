package com.theworkers.templatemicroservice.service.impl;

import com.theworkers.templatemicroservice.model.Role;
import com.theworkers.templatemicroservice.model.User;
import com.theworkers.templatemicroservice.model.input.UserInputDTO;
import com.theworkers.templatemicroservice.model.mappers.UserMapper;
import com.theworkers.templatemicroservice.model.output.WebResponse;
import com.theworkers.templatemicroservice.repository.RoleRepository;
import com.theworkers.templatemicroservice.repository.UserRepository;
import com.theworkers.templatemicroservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.theworkers.templatemicroservice.util.ValidateWebException;
import com.theworkers.templatemicroservice.util.validators.UserValidator;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserMapper userMapper,
                           UserValidator userValidator,
                           BCryptPasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
        this.bCryptPasswordEncoder = passwordEncoder;
    }

    public WebResponse createUser(UserInputDTO userInput) {
        try{
            userValidator.UserValidator(userInput);
            userValidator.validateUserRole(userInput.getRole(), roleRepository);
            userInput.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
            User newUser = userMapper.toEntity(userInput);
            Role existingRole = roleRepository.getById(userInput.getRole());
            newUser.setRole(existingRole);
            userRepository.save(newUser);
            return new WebResponse("The user is successfully created",
                                    HttpStatus.CREATED, null);
        }catch(ValidateWebException e){throw e;}
    }

    public WebResponse readUser(UserInputDTO user) {
        return null;
    }

    public WebResponse udpateUser(UserInputDTO user) {
        return null;
    }

    public WebResponse deleteUser(UUID userId) {
        return null;
    }
}
