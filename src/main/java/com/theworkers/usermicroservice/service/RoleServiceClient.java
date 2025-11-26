package com.theworkers.usermicroservice.service;

import com.theworkers.usermicroservice.model.output.RoleOutputDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "role-service", url = "${role.service.url}", configuration = FeignClientProperties.FeignClientConfiguration.class)
public interface RoleServiceClient {
    @GetMapping("/readById")
    RoleOutputDTO readRoleById(@RequestHeader("Authorization") String authorization,
                                           @RequestParam("roleId") Long roleId);
}
