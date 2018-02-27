package com.teamtreehouse.giflib.web;

public class FlashMessage {

    private String Message;
    private Status status;

    public FlashMessage(String message, Status status) {
        Message = message;
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public Status getStatus() {
        return status;
    }

    public static enum Status{
        SUCCESS, FAILURE
    }

}
