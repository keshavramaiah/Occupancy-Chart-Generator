package com.example.admin.occupancychart.Activities;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginTest {

    @Test
    public void validate() {
        String email = "achuth2000@hotmail.com";
        String password = "asdfgff";
        Boolean expected = true;

        Login logintest = new Login();
        Boolean output = logintest.validate(email,password);

        assertEquals(expected,output);
    }
}