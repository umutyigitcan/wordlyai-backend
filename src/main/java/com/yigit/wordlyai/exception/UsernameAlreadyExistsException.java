package com.yigit.wordlyai.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException() {
        super("Username is already in use");
    }
}
