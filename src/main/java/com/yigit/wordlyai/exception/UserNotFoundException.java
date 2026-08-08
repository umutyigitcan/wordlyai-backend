package com.yigit.wordlyai.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found");
    }
}
