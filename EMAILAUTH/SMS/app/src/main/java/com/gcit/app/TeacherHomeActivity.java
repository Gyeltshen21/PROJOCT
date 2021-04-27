package com.gcit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class TeacherHomeActivity extends AppCompatActivity {

    Button buttonLogout;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        buttonLogout = (Button) findViewById(R.id.logout_Btn);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intentHome = new Intent(TeacherHomeActivity.this,LoginActivity.class);
                startActivity(intentHome);
            }
        });
    }
    public void Notification(View view) {
        Toast.makeText(TeacherHomeActivity.this, "Notification Clicked", Toast.LENGTH_SHORT).show();
    }

    public void Result(View view) {
        Toast.makeText(TeacherHomeActivity.this, "Result Clicked", Toast.LENGTH_SHORT).show();
    }
}