package com.example.admin.occupancychart.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.admin.occupancychart.Models.Constants;
import com.example.admin.occupancychart.Models.MySingleton;
import com.example.admin.occupancychart.R;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private Button signup;
    private String[] teachers;
    private AutoCompleteTextView rollname;
    private String name, email, password,confirmpassword,location;
    private EditText emailedit,passwordedit,nameedit,passconfirmedit;
    private TextView alreadyuser,student,Teacher,Incharge;
    private boolean studclick,teacherclick,inchargeclick;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    ProgressDialog dialog;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        studclick= true;
        teacherclick=false;
        inchargeclick= false;
        dialog= new ProgressDialog(Register.this);
        student = findViewById(R.id.StudentSelect);
        Teacher = findViewById(R.id.TeacherSelect);
        Incharge = findViewById(R.id.InChargeSelect);
        signup = findViewById(R.id.SignUpBtn);
        rollname = findViewById(R.id.rollName);
        rollname.setVisibility(View.GONE);
        nameedit = (EditText)findViewById(R.id.NameEdit);
        emailedit = (EditText)findViewById(R.id.EmailEdit);
        passwordedit = (EditText)findViewById(R.id.PasswordEdit);
        alreadyuser= findViewById(R.id.already_user);
        passconfirmedit = findViewById(R.id.ConfirmPassword);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        progressBar = findViewById(R.id.SignupProgress);
        progressBar.setVisibility(View.INVISIBLE);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameedit.getText().toString().trim();
                email = emailedit.getText().toString().trim();
                password = passwordedit.getText().toString().trim();
                confirmpassword = passconfirmedit.getText().toString().trim();
                if(name.length()>0 && email.length()>0 && password.length()> 0 && confirmpassword.length()>0) {

                    if (email.contains("@")) {
                        if (password.equals(confirmpassword)) {
                            signup.setEnabled(false);
                                insertnewuser();
                                //Toast.makeText(getApplicationContext(), "Passwords match", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                            passwordedit.requestFocus();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please enter a valid email id", Toast.LENGTH_SHORT).show();
                        emailedit.requestFocus();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameedit.setVisibility(View.VISIBLE);
                rollname.setVisibility(View.GONE);
                nameedit.setHint("Roll number");
                studclick = true;
                teacherclick= false;
                inchargeclick = false;
                student.setBackgroundColor(getColor(R.color.white));
                Teacher.setBackgroundColor(getColor(R.color.background_color));
                Incharge.setBackgroundColor(getColor(R.color.background_color));
            }
        });

        Teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameedit.setVisibility(View.GONE);
                getTeachers();
                rollname.setVisibility(View.VISIBLE);
                teacherclick = true;
                studclick= false;
                inchargeclick = false;
                Teacher.setBackgroundColor(getColor(R.color.white));
                student.setBackgroundColor(getColor(R.color.background_color));
                Incharge.setBackgroundColor(getColor(R.color.background_color));
            }
        });

        Incharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameedit.setHint("Full Name");
                nameedit.setVisibility(View.VISIBLE);
                rollname.setVisibility(View.GONE);
                inchargeclick = true;
                studclick = false;
                teacherclick= false;
                Incharge.setBackgroundColor(getColor(R.color.white));
                Teacher.setBackgroundColor(getColor(R.color.background_color));
                student.setBackgroundColor(getColor(R.color.background_color));
            }
        });
        alreadyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }


    private void insertnewuser()
    {
        progressBar.setVisibility(View.VISIBLE);
        signup.setBackgroundColor(getColor(R.color.background_color));
        signup.setEnabled(false);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.REG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Response is : " + response.toString());
                if(response.toString().contains("Values inserted")) {
                    signup.setBackgroundColor(getColor(R.color.white_greyish));
                    progressBar.setVisibility(View.INVISIBLE);
                    editor.putString(Constants.KEY_EMAIL, email);
                    editor.commit();
                    if(teacherclick)
                        startActivity(new Intent(getApplicationContext(),TeacherHome.class));
                    else if (studclick)
                        startActivity(new Intent(getApplicationContext(),StudentHome.class));
                    else if (inchargeclick)
                        startActivity(new Intent(getApplicationContext(),InChargeHome.class));

                }
                else if(response.toString().contains("User already exists"))
                {
                    signup.setBackgroundColor(getColor(R.color.button_selectorcolor));
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"User already exits",Toast.LENGTH_SHORT).show();
                }
                signup.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println(error.toString());
                signup.setBackgroundColor(getColor(R.color.white_greyish));
                progressBar.setVisibility(View.INVISIBLE);
                signup.setEnabled(true);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();

                params.put(Constants.KEY_NAME,name);
                params.put(Constants.KEY_EMAIL,email);
                params.put(Constants.KEY_PASSWORD,password);
                if(studclick)
                    params.put(Constants.KEY_TYPE,"Student");
                else if(teacherclick)
                    params.put(Constants.KEY_TYPE, "Teacher");
                else if (inchargeclick)
                    params.put(Constants.KEY_TYPE, "InCharge");
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
public void getTeachers()
{
    dialog.setMessage("Getting list of teachers, please wait.");
    dialog.show();

    StringRequest request = new StringRequest(Request.Method.POST, Constants.TEACHER_URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            if (dialog.isShowing())
                dialog.dismiss();

            //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
            String rep =  response.toString();
            if (rep.contains("Error"))
            {

            }
            else
            {
                teachers = rep.split(",");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, teachers);
                //Getting the instance of AutoCompleteTextView
                rollname.setThreshold(1);//will start working from first character
                rollname.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                rollname.setTextColor(Color.WHITE);
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
            return params;
        }
    };

    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

}

    @Override
    protected void onStart() {
        super.onStart();
    }
}
