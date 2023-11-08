package com.laureapp.ui.card.Segnalazioni;

public class Chat {

    private String message;
    private Long receiver;
    private Long sender;

    public Chat(String message, Long receiver, Long sender) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }

    public Chat() {
        // Costruttore vuoto
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }
}