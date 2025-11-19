package com.theworkers.filesmicroservice.util.validators;

import org.springframework.http.HttpStatus;
import com.theworkers.filesmicroservice.util.ValidateWebException;

public class GeneralValidator {
    protected static final com.theworkers.templatemicroservice.util.validators.Validator notNullValidator = (value, fieldName) -> {
        if (value == null) {
            throw new ValidateWebException(
                    "That field '" + fieldName + "' can not be null.",
                    HttpStatus.BAD_REQUEST
            );
        }
    };
}
