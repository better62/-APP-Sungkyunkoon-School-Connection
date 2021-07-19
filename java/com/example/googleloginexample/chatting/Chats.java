package com.example.googleloginexample.chatting;

public class Chats {
    private String dataTime;
    private String textMessage;
    private String type;
    private String sender;
    private String receiver;

    public Chats() {
    }

    public Chats(String dataTime, String textMessage, String type, String sender, String receiver) {
        this.dataTime = dataTime;
        this.textMessage = textMessage;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;

    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

}
