package com.example.pursuit.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTest extends Message {

    Message myMessage = new Message("id", "senderId", "senderUsername", "recepId", "recepUsername", "txt", "created");

    @Test
    public void getSenderIdTest() {
        assert(myMessage.getSenderId().equals("senderId"));
    }

    @Test
    public void setSenderIdTest() {
        myMessage.setSenderId("newSenderId");
        assert(myMessage.getSenderId().equals("newSenderId"));
    }

    @Test
    public void getSenderUsernameTest() {
        assert(myMessage.getSenderUsername().equals("senderUsername"));
    }

    @Test
    public void setSenderUsernameTest() {
        myMessage.setSenderUsername("newSenderUsername");
        assert(myMessage.getSenderUsername().equals("newSenderUsername"));
   }

    @Test
    public void getMessageTextTest() {
        assert(myMessage.getMessageText().equals("txt"));
    }

    @Test
    public void setMessageTextTest() {
        myMessage.setMessageText("newMessageTxt");
        assert(myMessage.getMessageText().equals("newMessageTxt"));
    }

}