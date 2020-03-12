package com.example.admin.occupancychart.Activities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CancelTest {
    @Test
    public void Cancel() {
        CancelClass  roomActivity = new CancelClass();
        String input = "Class Cancelled";
        boolean expected = true;
        boolean output;
        if(input.contains("Class Cancelled"))
        {
            output=true;
        }
        else
        {
            output=false;
        }
        assertEquals(expected,output);
    }
}
