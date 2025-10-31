package com.theworkers.rolemicroservice.util.validators;

@FunctionalInterface
public interface Validator {
    void validate(Object value,String message);
}
