package com.theworkers.rolemicroservice.model.mappers;

import com.theworkers.rolemicroservice.model.Role;
import com.theworkers.rolemicroservice.model.input.RoleInputDTO;
import com.theworkers.rolemicroservice.model.output.RoleOutputDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleInputDTO dto);
    RoleOutputDTO toOutputEntity(Role entity);
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
