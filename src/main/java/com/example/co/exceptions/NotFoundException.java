package com.example.co.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class is used to throw Not found runtime exception
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends  RuntimeException{
    public NotFoundException(Class project, long id) {
        super("Requested id: " + id + " Not found.");
    }
}
