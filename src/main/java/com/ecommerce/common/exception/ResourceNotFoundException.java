package com.ecommerce.common.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String name) {
        super(name);
    }
}
