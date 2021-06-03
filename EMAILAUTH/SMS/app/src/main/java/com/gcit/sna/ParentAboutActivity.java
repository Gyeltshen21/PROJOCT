package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ParentAboutActivity extends AppCompatActivity {

    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_about);
        Intent intent = getIntent();
        String stdCode = intent.getStringExtra("stdCode");
        sCode = stdCode;
    }

    public void callBackParentHome(View view) {
        Intent intent = new Intent(getApplicationContext(),ParentHomeActivity.class);
        intent.putExtra("stdCode",sCode);
        startActivity(intent);
    }
}