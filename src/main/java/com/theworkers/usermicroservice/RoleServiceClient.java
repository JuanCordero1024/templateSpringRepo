package com.theworkers.usermicroservice;

import com.theworkers.usermicroservice.model.output.WebResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "role-service", url = "${roles.service.url}")
public interface RoleServiceClient {
    @GetMapping("/role/read")
    WebResponse readRole(@RequestParam("nameRole") String roleName);
}
