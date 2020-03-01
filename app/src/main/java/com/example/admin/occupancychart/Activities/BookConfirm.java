package com.example.admin.occupancychart.Activities;

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
import com.example.admin.occupancychart.Models.Period;
import com.example.admin.occupancychart.R;

import java.util.HashMap;
import java.util.Map;

public class BookConfirm extends AppCompatActivity {
private String room,day;
private int period;
private Button cancel,confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_confirm);
        Intent intent = getIntent();
        room = intent.getStringExtra("Room");
        day = intent.getStringExtra("Day");
        period  = intent.getIntExtra("Period",-1);
        cancel = findViewById(R.id.CancelButton);
        confirm = findViewById(R.id.ConfirmButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookroom();
                finish();
            }
        });
    }
    public void bookroom()
    {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.CONFIRMROOM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response is " + response);
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
                params.put(Constants.KEY_DAY,day);
                params.put(Constants.KEY_ROOM,room);
                params.put(Constants.KEY_PERIOD,String.valueOf(period));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
}
