package com.theworkers.usermicroservice.service;

import com.theworkers.usermicroservice.model.input.UserInputDTO;
import com.theworkers.usermicroservice.model.input.UserLoginInput;
import com.theworkers.usermicroservice.model.output.WebResponse;
import java.util.UUID;

public interface UserService {
    WebResponse createUser(UserInputDTO user);
    WebResponse readUser(UserInputDTO user);
    WebResponse udpateUser(UserInputDTO user);
    WebResponse deleteUser(UUID userId);
    WebResponse verifyUserCredentials(UserLoginInput credentials);
}
