package com.theworkers.usermicroservice.service.impl;

import com.theworkers.usermicroservice.model.enums.UserStatus;
import com.theworkers.usermicroservice.model.input.UserUpdateInputDTO;
import com.theworkers.usermicroservice.model.output.UserOutputDTO;
import com.theworkers.usermicroservice.model.output.UserSignOutputDTO;
import com.theworkers.usermicroservice.model.output.WebResponsePagination;
import com.theworkers.usermicroservice.service.RoleServiceClient;
import com.theworkers.usermicroservice.model.User;
import com.theworkers.usermicroservice.model.input.UserInputDTO;
import com.theworkers.usermicroservice.model.input.UserLoginInput;
import com.theworkers.usermicroservice.model.mappers.UserMapper;
import com.theworkers.usermicroservice.model.output.WebResponse;
import com.theworkers.usermicroservice.repository.UserRepository;
import com.theworkers.usermicroservice.service.UserService;
import com.theworkers.usermicroservice.util.CryptoHelper;
import com.theworkers.usermicroservice.util.specificators.UserSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.theworkers.usermicroservice.util.ValidateWebException;
import com.theworkers.usermicroservice.util.validators.UserValidator;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleServiceClient roleServiceClient;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CryptoHelper cryptoHelper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           UserValidator userValidator,
                           RoleServiceClient roleServiceClient,
                           BCryptPasswordEncoder passwordEncoder,
                           CryptoHelper cryptoHelper)
    {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleServiceClient = roleServiceClient;
        this.userValidator = userValidator;
        this.bCryptPasswordEncoder = passwordEncoder;
        this.cryptoHelper = cryptoHelper;
    }

    public WebResponse createUser(UserInputDTO userInput) {
        try{
            CryptoHelper.StringKeyPair keys = cryptoHelper.generateRSAKeys();
            userValidator.UserValidator(userInput);
            userInput.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
            User newUser = userMapper.toEntity(userInput);
            newUser.setRoleId(roleServiceClient.readRoleById(userInput.getRole()).getId());
            newUser.setPublicKey(keys.publicKey());
            newUser.setPrivateKey(keys.privateKey());
            userRepository.save(newUser);
            return new WebResponse("The user is successfully created",
                                    HttpStatus.CREATED, null);
        }catch(ValidateWebException e){throw e;}
    }

    public WebResponse readUser(UUID userId) {
        try{
            userValidator.validateUserByUUIDforExternalClient(userId, userRepository);
            return new WebResponse("User successfully read", HttpStatus.OK,
                    userMapper.toUserOutPutDTO(userRepository.findById(userId).get()));
        }catch(ValidateWebException e){throw e;}
    }

    public WebResponse readUserForSign(UUID userId) {
        try{
            userValidator.validateUserByUUIDforExternalClient(userId, userRepository);
            return new WebResponse("User successfully read", HttpStatus.OK,
                    userMapper.toUserSignOutputDTO(userRepository.findById(userId).get()));
        }catch(ValidateWebException e){throw e;}
    }

    public WebResponsePagination readUsers(int page, int size, Map<String, String> filters) {
        // BUILDING SPECIFICATION FOR QUERY
        Specification<User> spec = new UserSpecificationBuilder(filters).build();
        // DOING QUERY
        int adjustedPage = page > 0 ? page - 1 : 0;
        Page<User> paginatedResult = userRepository.findAll(spec, PageRequest.of(adjustedPage, size));

        List<UserOutputDTO> response = paginatedResult.getContent()
                .stream()
                .map(userMapper::toUserOutPutDTO)
                .collect(Collectors.toList());

        return new WebResponsePagination(
                paginatedResult.isEmpty()? "No data": "Retrieving data",
                HttpStatus.OK,
                response,
                paginatedResult.getTotalElements(),
                paginatedResult.getTotalPages(),
                paginatedResult.getNumber()+1
        );
    }

    public WebResponse udpateUser(String auth, UserUpdateInputDTO userInput, UUID userId) {
        try{
            userValidator.validateUserByUUID(userId, userRepository);
            User userFromBd = userRepository.getUserById(userId);
            if (userInput.getPassword() != null)
                userFromBd.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
            if (userInput.getRole() != null)
                userFromBd.setRoleId(roleServiceClient.readRoleById(userInput.getRole()).getId());
            userRepository.save(userFromBd);
            return new WebResponse("The user is successfully updated",
                    HttpStatus.CREATED, null);
        }catch(ValidateWebException e){throw e;}
    }

    public WebResponse deleteUser(UUID userId, UserStatus status) {
        try{
            userValidator.validateUserByUUID(userId, userRepository);
            User userToChange = userRepository.findById(userId).get();
            userToChange.setStatus(status);
            userRepository.save(userToChange);
            return new WebResponse("The user status changed to: " + status + " successfully",
                    HttpStatus.CREATED, null);
        }catch(ValidateWebException e){throw e;}      }

    public WebResponse verifyUserCredentials(UserLoginInput credentials) {
        try{
            userValidator.UserLoginValidator(credentials);
            if (userValidator.validateUserCrendentials(credentials,  userRepository, bCryptPasswordEncoder)) {
                return new WebResponse("Successfully logged in", HttpStatus.OK,
                        userMapper.toUserOutPutDTO(userRepository.getUserByEmail(credentials.getEmail())));
            }else {return new WebResponse("Password mismatch", HttpStatus.UNAUTHORIZED,null);}
        }catch(ValidateWebException e){throw e;}
    }

    public WebResponse setECCToken(UUID userId, String tokenECC) {
        try{
            if(userValidator.validateUserByUUID(userId, userRepository)){
                userValidator.validateUserIfHaveECCToken(userId, userRepository);
                User userToSetECCToken = userRepository.getUserById(userId);
                userToSetECCToken.setEccPublicKey(tokenECC);
                userRepository.save(userToSetECCToken);
                return new WebResponse("User token setted", HttpStatus.OK, null);
            }else{return new WebResponse("User not found", HttpStatus.UNAUTHORIZED,false);}
        }catch(ValidateWebException e){throw e;}
    }


}
