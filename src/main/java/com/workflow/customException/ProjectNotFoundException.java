package com.workflow.customException;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException() {
        super("Project Not found");
    }

    public ProjectNotFoundException(String message) {
        super(message);
    }
}