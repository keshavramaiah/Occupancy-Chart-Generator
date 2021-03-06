package com.example.admin.occupancychart.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class StudentHome extends AppCompatActivity {
    private int day;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private ProgressDialog dialog;
    private SharedPreferences pref ;
    private String classes[],periods[];
    private ArrayList<Period>listOfPeriods;
    private PeriodAdapter periodAdapter;
    private RecyclerView recyclerView;
    private String roll;
    private String[] times = new String[]{"0","8:40am-9:30am","9:30am-10:20am","10:20am-11:10am","11:20am-12:10pm","12:10pm-1:00pm","1:00pm-2:00pm","2:00pm-2:50pm","2:50pm-3:40pm","3:40pm-4:30pm","4:30pm-5:30pm"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        Calendar calendar = Calendar.getInstance();
        fab = findViewById(R.id.fab);
        listOfPeriods=new ArrayList<>();
        recyclerView = findViewById(R.id.PeriodRecycler);
        dialog= new ProgressDialog(StudentHome.this);
        day = 5 ;//calendar.get(Calendar.DAY_OF_WEEK)-1;
        System.out.println("Day is " + (calendar.get(Calendar.DAY_OF_WEEK)-1));
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        roll=pref.getString("ROLL",null);
        System.out.println("Roll is " + roll);
        swipeRefreshLayout = findViewById(R.id.SwipeRefresh);
        if(roll==null)
        {
            Toast.makeText(getApplicationContext(),"Registration error,please register again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Register.class));
        }
        else
            getData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BookRoom.class));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    public void getData()
    {
       swipeRefreshLayout.setRefreshing(true);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.STUDENTDATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefreshLayout.setRefreshing(false);
                if(listOfPeriods!=null)
                {
                    listOfPeriods.clear();
                }
                String rep =  response.toString();
                System.out.println("Rep length" + rep.length());
                String cr = rep.substring(0,1);
                System.out.println(cr);
                if(cr.equals("1"))
                {
                    fab.setVisibility(View.VISIBLE);
                }
                else
                    fab.setVisibility(View.GONE);
               System.out.println("Response is " + rep);
                if(rep.length()==5)
                    Toast.makeText(getApplicationContext(),"No classes today",Toast.LENGTH_SHORT).show();
                else if (rep.contains("Error"))
                {
                    Toast.makeText(getApplicationContext(),"An error as occurred",Toast.LENGTH_SHORT).show();
                }
                else if
                (rep.length()>5)
                {
                    System.out.println("Inside else");
                    rep = rep.substring(1);
                    String room = rep.substring(0,4);
                    rep = rep.substring(4);
                    System.out.println(room);
                    String []r = rep.split(";");
                    String []temp;
                    classes= new String[r.length];
                    periods=new String[r.length];
                    int i =0;
                    for (String s : r) {
                        temp=s.split(",");
                        periods[i]=temp[0];
                        classes[i]=temp[1];
                        periods[i]=times[Integer.valueOf(periods[i])];
                        listOfPeriods.add(new Period(room,periods[i],classes[i]));
                        i++;
                    }
                    dispadapter(listOfPeriods);
                }
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
                params.put(Constants.KEY_DAY,String.valueOf(day));
                params.put(Constants.KEY_ROLL,roll);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void dispadapter(ArrayList<Period> listperiods) {
        periodAdapter = new PeriodAdapter(getApplicationContext(),listperiods,false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(periodAdapter);
        periodAdapter.notifyDataSetChanged();
    }
}
