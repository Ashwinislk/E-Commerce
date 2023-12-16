package com.electronicstore.exception;

public class BadApiRequest extends RuntimeException{

    String message;

    public BadApiRequest(String message){
        super(message);
        this.message=message;
    }
}
