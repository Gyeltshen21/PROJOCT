package com.gcit.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherPDFActivity extends AppCompatActivity {
    RecyclerView TeacherRecycleView;
    TeacherPDFAdapter teacherPDFAdapter;
    FloatingActionButton TeacherFloatingBar;

    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_p_d_f);

        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        sCode = employeeID;

        TeacherFloatingBar = (FloatingActionButton)findViewById(R.id.TeacherFloatingBar);
        TeacherFloatingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TeacherUploadActivity.class);
                intent.putExtra("employeeID",sCode);
                startActivity(intent);
            }
        });

        TeacherRecycleView = (RecyclerView) findViewById(R.id.TeacherPDFRecycleView);
        TeacherRecycleView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TeacherPDFHelperClass> options =
                new FirebaseRecyclerOptions.Builder<TeacherPDFHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("TeacherPDFResult"),TeacherPDFHelperClass.class)
                .build();

        teacherPDFAdapter = new TeacherPDFAdapter(options);
        TeacherRecycleView.setAdapter(teacherPDFAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        teacherPDFAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        teacherPDFAdapter.stopListening();
    }

    public void BackToTeacherHome(View view) {
        Intent intent = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        intent.putExtra("employeeID",sCode);
        startActivity(intent);
    }
}