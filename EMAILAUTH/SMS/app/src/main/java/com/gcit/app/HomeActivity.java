package com.gcit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    Button buttonLogout;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    String s1, s2, s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String sCode1 = intent.getStringExtra("schoolCode");
        String email1 = intent.getStringExtra("email");
        String phoneNo1 = intent.getStringExtra("phoneNo");
        s1 = sCode1;
        s2 = email1;
        s3 = phoneNo1;
        buttonLogout = (Button) findViewById(R.id.logoutBtn);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intentHome = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intentHome);
            }
        });
    }
    public void Notification(View view) {
        Intent homeIntent = new Intent(this,AdminProfileActivity.class);
        homeIntent.putExtra("schoolCode",s1);
        homeIntent.putExtra("email",s2);
        homeIntent.putExtra("phoneNo",s3);
        startActivity(homeIntent);
    }

    public void Result(View view) {
        Toast.makeText(HomeActivity.this, "Result Clicked", Toast.LENGTH_SHORT).show();
    }

    public void CreateAccount(View view) {
        Intent intentHome = new Intent(HomeActivity.this,TeacherRegisterActivity.class);
        startActivity(intentHome);
    }
}