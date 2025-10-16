package com.theworkers.templatemicroservice.service;

import com.theworkers.templatemicroservice.model.input.UserInputDTO;
import com.theworkers.templatemicroservice.model.output.WebResponse;
import java.util.UUID;

public interface UserService {
    WebResponse createUser(UserInputDTO user);
    WebResponse readUser(UserInputDTO user);
    WebResponse udpateUser(UserInputDTO user);
    WebResponse deleteUser(UUID userId);
}
