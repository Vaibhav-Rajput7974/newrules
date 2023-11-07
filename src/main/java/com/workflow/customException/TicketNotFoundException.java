package com.workflow.customException;

public class TicketNotFoundException extends RuntimeException{

    public TicketNotFoundException() {
        super("Ticket not found");
    }

    public TicketNotFoundException(String message) {
        super(message);
    }

}
