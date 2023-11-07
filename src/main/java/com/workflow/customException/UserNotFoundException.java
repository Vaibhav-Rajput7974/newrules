package com.workflow.customException;

import com.workflow.entity.User;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
