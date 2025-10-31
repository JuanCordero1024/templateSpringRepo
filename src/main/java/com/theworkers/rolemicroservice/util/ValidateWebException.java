package com.theworkers.rolemicroservice.util;

import com.theworkers.rolemicroservice.model.output.WebResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidateWebException extends RuntimeException {
    private final WebResponse<?> webResponse;

    public ValidateWebException(String message, HttpStatus status) {
        super(message);
        this.webResponse = new WebResponse<>(message, status, null);
    }

}

