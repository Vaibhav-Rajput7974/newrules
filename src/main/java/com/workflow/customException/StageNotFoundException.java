package com.workflow.customException;

public class StageNotFoundException extends RuntimeException{

    public StageNotFoundException() {
        super("Stage not found");
    }
    public StageNotFoundException(String message) {
        super(message);
    }
}