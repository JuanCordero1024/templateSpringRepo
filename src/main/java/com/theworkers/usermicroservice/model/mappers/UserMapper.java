package com.theworkers.usermicroservice.model.mappers;

import com.theworkers.usermicroservice.model.User;
import com.theworkers.usermicroservice.model.input.UserInputDTO;
import com.theworkers.usermicroservice.model.input.UserLoginInput;
import com.theworkers.usermicroservice.model.input.UserUpdateInputDTO;
import com.theworkers.usermicroservice.model.output.UserOutputDTO;
import com.theworkers.usermicroservice.model.output.UserSignOutputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = RoleMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserOutputDTO toUserOutPutDTO(User user);
    UserSignOutputDTO toUserSignOutputDTO(User user);
    User toEntity(UserUpdateInputDTO user);
    User toEntity(UserInputDTO dto);
    UserInputDTO toUserInputDTO(UserLoginInput user);
}