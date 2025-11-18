package com.theworkers.templatemicroservice.util.validators;

@FunctionalInterface
public interface Validator {
    void validate(Object value,String message);
}
