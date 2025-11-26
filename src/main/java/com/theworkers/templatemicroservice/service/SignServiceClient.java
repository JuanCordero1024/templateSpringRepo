package com.theworkers.templatemicroservice.service;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "sign-service", url = "${sign.service.url}")
public interface SignServiceClient {
    @PostMapping(value = "/firmar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response signDocument(
            @RequestPart("usuario") String userId,
            @RequestPart("tipo") String tipo,
            @RequestPart("private_key") MultipartFile privateKey,
            @RequestPart("archivo") MultipartFile archivo
    );
}
