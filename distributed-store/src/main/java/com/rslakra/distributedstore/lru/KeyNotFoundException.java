package com.rslakra.distributedstore.lru;

public class KeyNotFoundException extends RuntimeException {
    
    public KeyNotFoundException(String message) {
        super(message);
    }
}
