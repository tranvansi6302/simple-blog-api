package com.simpleblogapi.simpleblogapi.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class Validator {
    public static Map<String,String> getMessageValidator(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : result.getFieldErrors()) {
            String field = ConvertUtil.convertToSnakeCase(error.getField());
            String message = error.getDefaultMessage();
            errors.put(field, message);
        }
        return errors;
    }
}
