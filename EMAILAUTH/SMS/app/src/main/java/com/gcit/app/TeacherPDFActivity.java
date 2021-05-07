package com.gcit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TeacherPDFActivity extends AppCompatActivity {

    FloatingActionButton TeacherFloatingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_p_d_f);

        TeacherFloatingBar = (FloatingActionButton)findViewById(R.id.TeacherFloatingBar);
        TeacherFloatingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TeacherUploadActivity.class));
            }
        });
    }

    public void BackToTeacherHome(View view) {
    }
}