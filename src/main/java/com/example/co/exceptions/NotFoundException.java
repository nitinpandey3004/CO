package com.example.co.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class is used to throw Not found runtime exception
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends  RuntimeException{

    public NotFoundException() {
        super();
    }
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(Class project, long id) {
        super("Requested id: " + id + " Not found.");
    }
}
