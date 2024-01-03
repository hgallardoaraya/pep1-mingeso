package com.mingeso.topeducation.exceptions;


public class RegistroNoExisteException extends IllegalArgumentException{
    public RegistroNoExisteException(String message){
        super(message);
    }
}
