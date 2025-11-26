package com.theworkers.templatemicroservice.model.mappers;

import com.theworkers.templatemicroservice.model.UserFile;
import com.theworkers.templatemicroservice.model.input.UserFileInputDTO;
import com.theworkers.templatemicroservice.model.output.UserFileOutputDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserFileMapper {
    UserFile toEntity(UserFileInputDTO userFileInputDTO);
    UserFileOutputDTO toOutput(UserFile userFileOutputDTO);
    List<UserFileOutputDTO> toOutputList(List<UserFile> userFilesResult);

}
