package com.example.admin.occupancychart.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.admin.occupancychart.Models.Constants;
import com.example.admin.occupancychart.Models.MySingleton;
import com.example.admin.occupancychart.Models.Period;
import com.example.admin.occupancychart.Models.PeriodAdapter;
import com.example.admin.occupancychart.R;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookRoom extends AppCompatActivity {
    String room[]={"None","A203","A204","A303","A304","C203","C204"};
    private Spinner spinner;
    private String roomselection;
    private ProgressDialog dialog;
    private int day;
    private ArrayList<Period>listOfPeriods;
    private String[] times = new String[]{"8:40am-9:30am","9:30am-10:20am","10:20am-11:10am","11:20am-12:10pm","12:10pm-1:00pm","0","2:00pm-2:50pm","2:50pm-3:40pm","3:40pm-4:30pm"};
    private ArrayList<Integer> periods;
    private PeriodAdapter periodAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK)-2;
        periods = new ArrayList<>();
        listOfPeriods = new ArrayList<>();
        periods.add(1);
        periods.add(2);
        periods.add(3);
        periods.add(4);
        periods.add(5);
        periods.add(7);
        periods.add(8);
        periods.add(9);
        spinner = findViewById(R.id.BookRoomSpinner);
        dialog= new ProgressDialog(BookRoom.this);
        recyclerView = findViewById(R.id.BookRoomRecycler);
        ArrayAdapter sRoomSpinner = new ArrayAdapter(this,android.R.layout.simple_spinner_item,room);
        sRoomSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(sRoomSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 roomselection = room[position];
                System.out.println("room is " + roomselection);
                if(position!=0)
                {
                    dialog.setMessage("Getting data, please wait.");
                    dialog.show();
                    getData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




    private void getData() {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.BOOKROOM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                if(listOfPeriods!=null)
                    listOfPeriods.clear();
                //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
                System.out.println("get book room data Response is : " + response.toString());
                String[] p  = response.split(";");
                    for (int i = 0; i < p.length; i++) {
                        periods.remove(Integer.valueOf(p[i]));
                    }

                    for (Integer period : periods) {
                        System.out.println(period);
                        String tmp = times[period - 1];
                        listOfPeriods.add(new Period(roomselection, period + "hr", tmp));

                    }
                    recyclerView.refreshDrawableState();
                    dispadapter(listOfPeriods);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();
                params.put(Constants.KEY_ROOM,roomselection);
                params.put(Constants.KEY_DAY,String.valueOf(day));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
    private void dispadapter(ArrayList<Period> listperiods) {
        periodAdapter = new PeriodAdapter(getApplicationContext(),listperiods);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(periodAdapter);
        periodAdapter.notifyDataSetChanged();
    }
}
