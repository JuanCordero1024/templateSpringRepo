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
            if(verifyCredentials.getCodeStatus().equals(HttpStatus.UNAUTHORIZED.value())){return verifyCredentials;}
            UserOutputDTO userDetails = objectMapper.convertValue(verifyCredentials.getEntity(), UserOutputDTO.class);
            String token = jWTService.generateToken(userDetails.getId(), credentials.getEccToken());
            userDetails.setToken(token);
            userDetails.setExpirationMs(expirationMs);
            String bearerToken = "Bearer " + token;
            EccInputDTO eccRequest = new EccInputDTO();
            eccRequest.setPublicKey(credentials.getEccToken());

            WebResponse setECCToken = userServiceClient.setEccToken(
                    bearerToken,
                    eccRequest,
                    userDetails.getId()
            );
            log.info("\n\nRespuesta del API: " + setECCToken.getCodeStatus() + "\n\n");
            if (setECCToken.getCodeStatus() == null & setECCToken.getCodeStatus() !=  HttpStatus.OK) {
                System.out.println("Error del API User: " + setECCToken.getMessage());
                throw new ValidateWebException("El login fue exitoso, pero no se pudo guardar la clave ECC.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new WebResponse("Successfully Logged In", HttpStatus.OK, userDetails);
        }catch(ValidateWebException e){throw e;}
    }
}
