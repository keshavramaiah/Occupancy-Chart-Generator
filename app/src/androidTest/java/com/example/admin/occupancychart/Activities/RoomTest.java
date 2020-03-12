package com.example.admin.occupancychart.Activities;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoomTest {
    @Rule
    public ActivityTestRule<RoomActivity> mActivityRule = new ActivityTestRule<>(
            RoomActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.example.admin.occupancychart", appContext.getPackageName());
    }

    @Test
    public void isspinner()
    {
        //onView(withId(R.id.s1)).perform();
    }
}
