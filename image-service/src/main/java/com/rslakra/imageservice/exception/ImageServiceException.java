package com.rslakra.imageservice.exception;

/**
 *
 */
public class ImageServiceException extends RuntimeException {

    /**
     * @param message
     */
    public ImageServiceException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public ImageServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
