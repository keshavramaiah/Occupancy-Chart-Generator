package com.example.admin.occupancychart.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.admin.occupancychart.Models.Constants;
import com.example.admin.occupancychart.Models.MySingleton;
import com.example.admin.occupancychart.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StudentHome extends AppCompatActivity {
    private int day;
    private ProgressDialog dialog;
    private SharedPreferences pref ;
    private String roll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        Calendar calendar = Calendar.getInstance();
        dialog= new ProgressDialog(StudentHome.this);
        day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        roll=pref.getString("ROLL",null);
        if(roll==null)
        {
            Toast.makeText(getApplicationContext(),"Registration error,please register again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Register.class));
        }
        else
            getData();
    }
    public void getData()
    {
        dialog.setMessage("Getting data, please wait.");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Constants.STUDENTDATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (dialog.isShowing())
                    dialog.dismiss();

                //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
                String rep =  response.toString();
                System.out.println("Response is " + rep);
                if(rep.length()==0)
                    Toast.makeText(getApplicationContext(),"No classes today",Toast.LENGTH_SHORT).show();
                if (rep.contains("Error"))
                {

                }
                else
                {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
                //progressBar.setVisibility(View.INVISIBLE);
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
}
