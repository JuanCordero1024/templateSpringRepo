package com.theworkers.rolemicroservice.service;

import com.theworkers.rolemicroservice.model.input.RoleInputDTO;
import com.theworkers.rolemicroservice.model.output.RoleOutputDTO;
import com.theworkers.rolemicroservice.model.output.WebResponse;

public interface RoleService {
    WebResponse createRole(RoleInputDTO roleInput);
    WebResponse readRole(String roleInput);
    RoleOutputDTO readRoleById(Long roleId);
    WebResponse udpateRole(RoleInputDTO roleInput);
    WebResponse deleteRole(Long roleId);

}
