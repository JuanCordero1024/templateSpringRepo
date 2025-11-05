package com.theworkers.usermicroservice.util.validators;

@FunctionalInterface
public interface Validator {
    void validate(Object value,String message);
}
