package com.example.ecommerce.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String message){
        super(message);
    }
}
