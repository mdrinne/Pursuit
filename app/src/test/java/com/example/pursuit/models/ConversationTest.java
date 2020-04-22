package com.example.pursuit.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConversationTest extends Conversation {
    private Conversation convo = new Conversation("myId", "otherId", "otherName", "otherRole", "ig", "nore");

    @Test
    public void getIdTest() { assert(convo.getId().equals("myId"));}

    @Test
    public void getOtherUserIdTest() {assert(convo.getOtherUserId().equals("otherId"));}

    @Test
    public void setOtherUserIdTest() {
        convo.setOtherUserId("newId");
        assert(convo.getOtherUserId().equals("newId"));
    }

    @Test
    public void getOtherUserRoleTest() { assert(convo.getOtherUserRole().equals("otherRole"));}

    @Test
    public void setOtherUserRoleTest() {
        convo.setOtherUserRole("newRole");
        assert(convo.getOtherUserRole().equals("newRole"));
    }



}