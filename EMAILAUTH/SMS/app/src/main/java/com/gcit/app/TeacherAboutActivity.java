package com.gcit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TeacherAboutActivity extends AppCompatActivity {

    String s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_about);
        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        s1 = employeeID;
    }

    public void callBackTeacherHome(View view) {
        Intent password = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        password.putExtra("employeeID",s1);
        startActivity(password);
    }
}