package com.theworkers.templatemicroservice.model.mappers;

import com.theworkers.templatemicroservice.model.User;
import com.theworkers.templatemicroservice.model.input.UserInputDTO;
import com.theworkers.templatemicroservice.model.output.UserOutputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = RoleMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserOutputDTO toUserOutPutDTO(User user);
    User toEntity(UserInputDTO dto);
}

