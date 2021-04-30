package com.gcit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AdminProfileActivity extends AppCompatActivity {

    //private ImageView AdminProfilePic;
    private TextView AdminHeaderName, AdminName, AdminSchoolCode, AdminEmail, AdminPhoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        //AdminProfilePic = (ImageView) findViewById(R.id.AdminProfilePic);
        AdminHeaderName = (TextView) findViewById(R.id.AdminHeaderName);
        AdminName = (TextView) findViewById(R.id.AdminName);
        AdminSchoolCode = (TextView) findViewById(R.id.AdminCode);
        AdminEmail = (TextView) findViewById(R.id.AdminEmail);
        AdminPhoneNo = (TextView) findViewById(R.id.AdminPhoneNo);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String schoolCode = intent.getStringExtra("schoolCode");
        String email = intent.getStringExtra("email");
        String phoneNo = intent.getStringExtra("phoneNo");
        String a1 = name;
        String b1 = schoolCode;
        String c1 = email;
        String d1 = phoneNo;
        AdminHeaderName.setText(a1);
        AdminName.setText(a1);
        AdminSchoolCode.setText(b1);
        AdminEmail.setText(c1);
        AdminPhoneNo.setText(d1);
    }

    public void GoBackToHome(View view) {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }
}