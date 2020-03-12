package com.example.admin.occupancychart.Activities;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterTest {

    @Test
    public void validate() {
        String name = "Achuth";
        String rollno ="CB.EN.U4CSE17602";
        String email = "achuth2000@hotmail.com";
        String password = "123456";
        String conpassword =  "123456";
        Boolean expected = true;
        Register registerobj = new Register();
        boolean output = registerobj.validate(name,rollno,email,password,conpassword);
        assertEquals(expected,output);
    }
}