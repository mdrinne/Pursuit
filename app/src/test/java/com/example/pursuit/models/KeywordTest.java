package com.example.pursuit.models;

import org.junit.Test;

import java.security.Key;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class KeywordTest {
    ArrayList<String> x = new ArrayList<String>(0);
    ArrayList<String> y = new ArrayList<String>(0);
    Keyword keyWord = new Keyword("id", "txt", x, y);

    @Test
    public void getIdTest() {
        assert(keyWord.getId().equals("id"));
    }

    @Test
    public void setIdTest() {
        keyWord.setId("newId");
        assert(keyWord.getId().equals("newId"));
    }

    @Test
    public void getTextTest() {
        assert(keyWord.getText().equals("txt"));
    }

    @Test
    public void setTextTest() {
        keyWord.setText("newText");
        assert(keyWord.getText().equals("newText"));
    }

    @Test
    public void getOpportunitiesTest() {
        assert(keyWord.getOpportunities().equals(x));
    }

    @Test
    public void setOpportunitiesTest() {
        keyWord.setOpportunities(y);
        assert(keyWord.getOpportunities().equals(y));
    }
}