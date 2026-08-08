package com.yigit.wordlyai.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid username, email, or password");
    }
}
