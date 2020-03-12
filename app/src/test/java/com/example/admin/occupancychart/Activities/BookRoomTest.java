package com.example.admin.occupancychart.Activities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BookRoomTest {

    @Test
    public void getExtracpy() {
        BookRoom   roomActivity = new BookRoom();
        String input = "1;3;5";
        boolean expected = true;
        boolean output = true;
        String[] input1 = input.split(";");
        String[] expectedarray = {"1","3","5"};

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
