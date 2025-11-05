package com.theworkers.usermicroservice.model.mappers;

import com.theworkers.usermicroservice.model.Role;
import com.theworkers.usermicroservice.model.input.RoleInputDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleInputDTO dto);

    // USED INTO MAPPER OF USER_MAPPER
    default Role fromId(Long id) {
        if (id == null) return null;
        Role r = new Role();
        r.setId(id);
        return r;
    }

    default String map(Role role) {
        if (!role.equals(null)) {
            return role.getName();
        }
        return null;
    }

}
