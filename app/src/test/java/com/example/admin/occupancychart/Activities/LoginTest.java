package com.example.admin.occupancychart.Activities;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginTest {

    @Test
    public void validateCorrect() {
        String email = "achuth2000@hotmail.com";
        String password = "asdfghjkl";
        Boolean expected = true;

        Login logintest = new Login();
        Boolean output = logintest.validate(email,password);

        assertEquals(expected,output);
    }


    @Test
    public void validateInComplete() {
        String email = "achuth2000@hotmail.com";
        String password = "";
        Boolean expected = false;

        Login logintest = new Login();
        Boolean output = logintest.validate(email,password);

        assertEquals(expected,output);
    }
}