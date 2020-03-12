package com.example.admin.occupancychart.Activities;

import org.junit.Test;

import static org.junit.Assert.*;

public class RoomActivityTest {

    @Test
    public void getExtracpy() {
     //   RoomActivity roomActivity = new RoomActivity();
        String input = "A303:C203:";
        boolean expected = true;
        boolean output = true;
        String[] input1 = input.split(":");
        String[] expectedarray = {"A303","C203"};

        for (int i = 0; i < input1.length; i++) {
            if(!input1[i].equals(expectedarray[i]))
            {
                output = false;
                break;
            }
        }
        assertEquals(expected,output);

    }
}