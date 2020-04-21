package com.example.pursuit.models;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ShareTest extends Share {

    ArrayList<String> x = new ArrayList<String>(0);
    ArrayList<String> y = new ArrayList<String>(0);
    Share myShare = new Share("1", "2", "3", "4", "5", "6", "7", "8", x, 0, "created");

    @Test
    public void getIdTest() {
        assert(myShare.getId().equals("1"));
    }

    @Test
    public void setIdTest() {
        myShare.setId("newId");
        assert(myShare.getId().equals("newId"));
    }

    @Test
    public void getInterestKeywordsTest() {
        assert(myShare.getInterestKeywords().equals(x));
    }

    @Test
    public void setInterestKeywordsTest() {
        myShare.setInterestKeywords(y);
        assert(myShare.getInterestKeywords().equals(y));
    }

    @Test
    public void getLikesTest() {
        assert(myShare.getLikes() == 0);
    }

    @Test
    public void setLikesTest() {
        myShare.setLikes(1);
        assert(myShare.getLikes() == 1);
    }

    @Test
    public void getCreatedAtTest() {
        assert(myShare.getCreatedAt().equals("created"));
    }

    @Test
    public void setCreatedAtTest() {
        myShare.setCreatedAt("newCreated");
        assert(myShare.getCreatedAt().equals("newCreated"));
    }
}