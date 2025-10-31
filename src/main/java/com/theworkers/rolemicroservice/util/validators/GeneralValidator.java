package com.theworkers.rolemicroservice.util.validators;

import org.springframework.http.HttpStatus;
import com.theworkers.rolemicroservice.util.ValidateWebException;

public class GeneralValidator {
    protected static final Validator notNullValidator = (value, fieldName) -> {
        if (value == null) {
            throw new ValidateWebException(
                    "That field '" + fieldName + "' can not be null.",
                    HttpStatus.BAD_REQUEST
            );
        }
    };
}
