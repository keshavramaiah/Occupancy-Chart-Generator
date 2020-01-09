package com.example.admin.occupancychart.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
    private String name, email, password,confirmpassword,location;
    private EditText emailedit,passwordedit,nameedit,passconfirmedit;
    private TextView alreadyuser,student,Teacher,Incharge;
    private boolean studclick,teacherclick,inchargeclick;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        studclick= true;
        teacherclick=false;
        inchargeclick= false;
        student = findViewById(R.id.StudentSelect);
        Teacher = findViewById(R.id.TeacherSelect);
        Incharge = findViewById(R.id.InChargeSelect);
        signup = findViewById(R.id.SignUpBtn);
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
                nameedit.setHint("Roll No");
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
                nameedit.setHint("Full Name");
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
//    public void showChangeLangDialog() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.popup, null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText edt = (EditText) dialogView.findViewById(R.id.edt_comment);
//
//        dialogBuilder.setTitle("Add location");
//        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                if (edt.getText().toString().length() > 0) {
//                    location = edt.getText().toString().trim();
//                    editor.putString("location", location);
//                    editor.commit();
//                    insertnewuser();
//                } else {
//                    showChangeLangDialog();
//                    Toast.makeText(getApplicationContext(),"Enter a  location to proceed",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//                signup.setEnabled(true);
//            }
//        });
//        AlertDialog b = dialogBuilder.create();
//        b.show();
//    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
