package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminAboutActivity extends AppCompatActivity {
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_about);
        Intent intent = getIntent();
        String Code = intent.getStringExtra("schoolCode");
        sCode = Code;
    }

    public void callBackHome(View view) {
        Intent intentHome = new Intent(this,HomeActivity.class);
        intentHome.putExtra("schoolCode",sCode);
        startActivity(intentHome);
    }
}