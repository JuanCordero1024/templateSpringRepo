package com.theworkers.usermicroservice.service;

import com.theworkers.usermicroservice.model.enums.UserStatus;
import com.theworkers.usermicroservice.model.input.UserInputDTO;
import com.theworkers.usermicroservice.model.input.UserLoginInput;
import com.theworkers.usermicroservice.model.input.UserUpdateInputDTO;
import com.theworkers.usermicroservice.model.output.WebResponse;
import com.theworkers.usermicroservice.model.output.WebResponsePagination;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    WebResponse createUser(UserInputDTO user);
    WebResponse readUser(UUID userId);
    WebResponse readUserForSign(UUID userId);
    WebResponsePagination readUsers(int page, int size, Map<String, String> filter);
    WebResponse udpateUser(String jwt, UserUpdateInputDTO user, UUID userId);
    WebResponse deleteUser(UUID userId, UserStatus status);
    WebResponse verifyUserCredentials(UserLoginInput credentials);
    WebResponse setECCToken(UUID userId, String tokenECC);
}
