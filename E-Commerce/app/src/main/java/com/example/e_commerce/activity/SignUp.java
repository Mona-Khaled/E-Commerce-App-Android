package com.example.e_commerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.e_commerce.Database.MyDatabase;
import com.example.e_commerce.Model.CustomerModel;
import com.example.e_commerce.R;

public class SignUp extends AppCompatActivity {

    EditText username , email,password,jop;
    Button signup_btn;
    Spinner gender;
    MyDatabase database;
    CalendarView cv;
    public String curDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        intiView();

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                curDate = String.valueOf(dayOfMonth)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
            }
        });


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        
    }
    
    
    protected void intiView()
    {
        username=findViewById(R.id.username_signup);
        email=findViewById(R.id.email_signup);
        password=findViewById(R.id.password_signup);
        jop=findViewById(R.id.jop_signup);
        gender=findViewById(R.id.gender);
        signup_btn=findViewById(R.id.btn_signup);
        database=new MyDatabase(this);
        cv=findViewById(R.id.calendarView);
    }
    
    
    protected void signUp() {
        String name = username.getText().toString().trim();
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String gen = gender.getSelectedItem().toString();
        String joptitle=jop.getText().toString();


            if (name.equals("") || mail.equals("") || pass.equals(""))
                Toast.makeText(this, "Some fields not entered", Toast.LENGTH_SHORT).show();
            else {
                 CustomerModel customerModel=new CustomerModel(name,mail,pass,gen,joptitle,curDate);
                database.insertCustomer(customerModel);
                Toast.makeText(this, "Successfully registered ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }


        }
    }
