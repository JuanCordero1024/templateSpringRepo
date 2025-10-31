package com.theworkers.rolemicroservice.service;

import com.theworkers.rolemicroservice.model.input.RoleInputDTO;
import com.theworkers.rolemicroservice.model.output.WebResponse;

public interface RoleService {
    WebResponse createRole(RoleInputDTO roleInput);
    WebResponse readRole(String roleInput);
    WebResponse udpateRole(RoleInputDTO roleInput);
    WebResponse deleteRole(Long roleId);

}
