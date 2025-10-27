package com.rslakra.imageservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImageNotFoundException extends RuntimeException {
    /**
     * @param message
     */
    public ImageNotFoundException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public ImageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
