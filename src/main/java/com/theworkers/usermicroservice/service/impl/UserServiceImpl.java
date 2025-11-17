package com.theworkers.usermicroservice.service.impl;

import com.theworkers.usermicroservice.RoleServiceClient;
import com.theworkers.usermicroservice.model.Role;
import com.theworkers.usermicroservice.model.User;
import com.theworkers.usermicroservice.model.input.UserInputDTO;
import com.theworkers.usermicroservice.model.input.UserLoginInput;
import com.theworkers.usermicroservice.model.mappers.RoleMapper;
import com.theworkers.usermicroservice.model.mappers.UserMapper;
import com.theworkers.usermicroservice.model.output.WebResponse;
import com.theworkers.usermicroservice.repository.RoleRepository;
import com.theworkers.usermicroservice.repository.UserRepository;
import com.theworkers.usermicroservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.theworkers.usermicroservice.util.ValidateWebException;
import com.theworkers.usermicroservice.util.validators.UserValidator;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleServiceClient roleServiceClient;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleMapper roleMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           UserValidator userValidator,
                           RoleServiceClient roleServiceClient,
                           BCryptPasswordEncoder passwordEncoder, RoleMapper roleMapper)
    {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleServiceClient = roleServiceClient;
        this.userValidator = userValidator;
        this.bCryptPasswordEncoder = passwordEncoder;
        this.roleMapper = roleMapper;
    }

    public WebResponse createUser(UserInputDTO userInput) {
        try{
            userValidator.UserValidator(userInput);
            userInput.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
            User newUser = userMapper.toEntity(userInput);
            newUser.setRoleId(roleServiceClient.readRoleById(userInput.getRole()).getId());
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

    public WebResponse verifyUserCredentials(UserLoginInput credentials) {
        try{
            if (userValidator.validateUserCrendentials(credentials,  userRepository, bCryptPasswordEncoder)) {
                return new WebResponse("Successfully logged in", HttpStatus.OK,
                        userMapper.toUserOutPutDTO(userRepository.getUserByEmail(credentials.getEmail())));
            }else {return new WebResponse("Password or email not found", HttpStatus.UNAUTHORIZED,false);}
        }catch(ValidateWebException e){throw e;}
    }
}
