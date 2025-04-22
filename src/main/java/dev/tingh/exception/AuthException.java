package dev.tingh.exception;

public class AuthException extends RuntimeException {

    public AuthException(Exception e) {
        super(e);
    }
}
