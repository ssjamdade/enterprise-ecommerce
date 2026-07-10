package com.ecommerce.common.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String name, int id) {
        super(name + " not found with id: " + id);
    }
}
