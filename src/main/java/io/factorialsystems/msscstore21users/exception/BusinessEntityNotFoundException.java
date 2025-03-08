package io.factorialsystems.msscstore21users.exception;

public class BusinessEntityNotFoundException extends RuntimeException{
    public BusinessEntityNotFoundException(String msg) {
        super(msg);
    }
    public BusinessEntityNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
