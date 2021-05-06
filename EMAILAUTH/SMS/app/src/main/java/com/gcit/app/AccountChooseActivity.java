package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountChooseActivity extends AppCompatActivity {
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_choose);

        Intent intent = getIntent();
        String Code = intent.getStringExtra("schoolCode");
        sCode = Code;
    }


    public void chooseVerificationTochooseRegister(View view) {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }

    public void teacherAccount(View view) {
        Intent intentHome = new Intent(AccountChooseActivity.this,AdminTeacherDetailsActivity.class);
        intentHome.putExtra("schoolCode",sCode);
        startActivity(intentHome);
    }

    public void parentAccount(View view) {
        Intent intentHome = new Intent(AccountChooseActivity.this,AdminParentDetailsActivity.class);
        intentHome.putExtra("schoolCode",sCode);
        startActivity(intentHome);
    }
}