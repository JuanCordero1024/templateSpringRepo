package com.theworkers.templatemicroservice.service;

import com.theworkers.templatemicroservice.model.output.WebResponse;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${user.service.url}")
@Service
public interface UserServiceClient {

    @GetMapping("/read/for/sign/{userId}")
    WebResponse readUser(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable("userId") UUID userId
    );

}
