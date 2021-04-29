package com.gcit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AdminProfileActivity extends AppCompatActivity {

    //private ImageView AdminProfilePic;
    private TextView AdminName, AdminSchoolCode, AdminEmail, AdminPhoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        //AdminProfilePic = (ImageView) findViewById(R.id.AdminProfilePic);
        AdminName = (TextView) findViewById(R.id.AdminName);
        AdminSchoolCode = (TextView) findViewById(R.id.AdminCode);
        AdminEmail = (TextView) findViewById(R.id.AdminEmail);
        AdminPhoneNo = (TextView) findViewById(R.id.AdminPhoneNo);

        Intent intent = getIntent();
        String schoolCode = intent.getStringExtra("schoolCode");
        String email = intent.getStringExtra("email");
        String phoneNo = intent.getStringExtra("phoneNo");
        String a1 = schoolCode;
        String b1 = email;
        String c1 = phoneNo;
        AdminSchoolCode.setText("SchoolCode: " +a1);
        AdminEmail.setText("Email " +b1);
        AdminPhoneNo.setText("Phone Number " +c1);
    }
}