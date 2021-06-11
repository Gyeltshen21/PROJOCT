package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherNotificationActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private TeacherNotificationAdapter TeacherNotificationAdapter;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_notification);

        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        sCode = employeeID;

        recyclerView = (RecyclerView) findViewById(R.id.TeacherNotificationRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<AdminNotificationHelperClass> options =
                new FirebaseRecyclerOptions.Builder<AdminNotificationHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TeacherNews"),AdminNotificationHelperClass.class)
                        .build();
        TeacherNotificationAdapter = new TeacherNotificationAdapter(options);
        recyclerView.setAdapter(TeacherNotificationAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TeacherNotificationAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        TeacherNotificationAdapter.stopListening();
    }
    public void BackToTeacherNotificationHome(View view) {
        Intent intent = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        intent.putExtra("employeeID",sCode);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }
}