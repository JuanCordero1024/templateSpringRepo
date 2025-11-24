package com.theworkers.templatemicroservice.service;

import com.theworkers.templatemicroservice.model.input.EccInputDTO;
import com.theworkers.templatemicroservice.model.input.UserLoginInput;
import com.theworkers.templatemicroservice.model.output.WebResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${user.service.url}")
@Service
public interface UserServiceClient {
    @PostMapping("/verify-credentials")
    @Headers("Content-Type: application/json")
    WebResponse verifyCredentials(@RequestBody UserLoginInput credentials);

    @PostMapping("/setEccToken/{userId}")
    WebResponse setEccToken(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody EccInputDTO request,
            @PathVariable("userId") UUID userId
    );

}
