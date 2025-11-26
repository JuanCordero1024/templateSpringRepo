package com.theworkers.templatemicroservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theworkers.templatemicroservice.model.input.EccInputDTO;
import com.theworkers.templatemicroservice.model.input.UserLoginInput;
import com.theworkers.templatemicroservice.model.output.UserOutputDTO;
import com.theworkers.templatemicroservice.model.output.WebResponse;
import com.theworkers.templatemicroservice.service.AuthService;
import com.theworkers.templatemicroservice.service.JWTService;
import com.theworkers.templatemicroservice.service.UserServiceClient;
import com.theworkers.templatemicroservice.util.ValidateWebException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;
    private final JWTService jWTService;

    @Value("${jwt.expiration-ms}")
    private Integer expirationMs;

    public AuthServiceImpl(UserServiceClient userServiceClient,
                          JWTService jWTService, ObjectMapper objectMapper) {
        this.userServiceClient = userServiceClient;
        this.objectMapper = objectMapper;
        this.jWTService = jWTService;
    }

    public WebResponse login(UserLoginInput credentials) {
        try{
            WebResponse verifyCredentials = userServiceClient.verifyCredentials(credentials);
            if(verifyCredentials.getCodeStatus().equals(HttpStatus.UNAUTHORIZED)){return verifyCredentials;}
            UserOutputDTO userDetails = objectMapper.convertValue(verifyCredentials.getEntity(), UserOutputDTO.class);
            String token = jWTService.generateToken(userDetails.getId());
            userDetails.setToken(token);
            userDetails.setExpirationMs(expirationMs);
            return new WebResponse("Successfully Logged In", HttpStatus.OK, userDetails);
        }catch(ValidateWebException e){throw e;}
    }
}
