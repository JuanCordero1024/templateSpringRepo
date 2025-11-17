package com.theworkers.usermicroservice;

import com.theworkers.usermicroservice.model.output.RoleOutputDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "role-service", url = "${role.service.url}")
public interface RoleServiceClient {
    @GetMapping("/readById")
    RoleOutputDTO readRoleById(@RequestParam("roleId") Long roleId);
}
