package com.theworkers.templatemicroservice.service;

import com.theworkers.templatemicroservice.model.enums.FileStatus;
import com.theworkers.templatemicroservice.model.input.UserFileInputDTO;
import com.theworkers.templatemicroservice.model.input.UserFileSignInputDTO;
import com.theworkers.templatemicroservice.model.output.WebResponse;
import com.theworkers.templatemicroservice.model.output.WebResponsePagination;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public interface UserFileService {
    WebResponse uploadFile(String jwtToken,UserFileInputDTO newUserFileInputDTO, MultipartFile file);
    ResponseEntity<Resource> downloadFile(String jwtToken, UUID userFileId, FileStatus fileStatus);
    ResponseEntity<Resource> previewFile(String jwtToken, UUID userFileId, FileStatus fileStatus);
    WebResponse signFile(String jwtToken,UserFileSignInputDTO newUserFileSignInputDTO);
    WebResponse getFilesByUserId(String jwtToken,UUID userId);
    WebResponsePagination getFiles(String jwtToken, int page, int pageSize, Map<String, String> queryParams);
}
