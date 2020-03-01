package com.example.admin.occupancychart.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.admin.occupancychart.Models.Constants;
import com.example.admin.occupancychart.Models.MySingleton;
import com.example.admin.occupancychart.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CancelClass extends AppCompatActivity {
    private String[] times = new String[]{"0","8:40am-9:30am","9:30am-10:20am","10:20am-11:10am","11:20am-12:10pm","12:10pm-1:00pm","2:00pm-2:50pm","2:50pm-3:40pm","3:40pm-4:30pm","4:30pm-5:30pm",};
    private ArrayList<String> t;
    private String room;
    private String period;
    private int p;
    private Button cancel;
    private Button confirm;
    private ProgressDialog dialog;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_class);
        Calendar calendar = Calendar.getInstance();
        //day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        day = 5;
        Intent i = getIntent();
        dialog = new ProgressDialog(CancelClass.this);
        period = i.getStringExtra("Period");
        room = i.getStringExtra("Room");
        t = new ArrayList<>();
        cancel = findViewById(R.id.CancelClassButton);
        confirm = findViewById(R.id.ConfirmClassCancelButton);
        Collections.addAll(t, times);
        p = t.indexOf(period);
        //System.out.println("Period number is " + p);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Cancelling class, please wait.");
                dialog.show();
                cancelClass();
            }
        });

cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});
    }




    private void cancelClass() {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.CANCELCLASSES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response is " + response);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    if(response.contains("Class Cancelled"))
                        finish();
                    else
                    {
                        Toast.makeText(getApplicationContext(),"An error has occurred",Toast.LENGTH_SHORT).show();
                        finish();
                    }
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
                params.put(Constants.KEY_ROOM,room);
                params.put(Constants.KEY_PERIOD,String.valueOf(p));
                params.put(Constants.KEY_DAY,String.valueOf(day));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
}
