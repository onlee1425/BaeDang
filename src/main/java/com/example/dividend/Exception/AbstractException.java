package com.example.dividend.Exception;

public abstract class AbstractException extends RuntimeException{

    abstract public int getStatusCode();
    abstract public String getMessage();
}
