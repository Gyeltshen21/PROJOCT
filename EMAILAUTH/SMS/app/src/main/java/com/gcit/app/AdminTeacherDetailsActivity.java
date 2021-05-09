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

public class AdminTeacherDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    AdminTeacherAdapter adminTeacherAdapter;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher_details);

        Intent intent = getIntent();
        String Code = intent.getStringExtra("schoolCode");
        sCode = Code;

        recyclerView = (RecyclerView) findViewById(R.id.AdminTeacherRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TeacherUserHelperClass> options =
                new FirebaseRecyclerOptions.Builder<TeacherUserHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("employee"),TeacherUserHelperClass.class)
                .build();
        adminTeacherAdapter = new AdminTeacherAdapter(options);
        recyclerView.setAdapter(adminTeacherAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adminTeacherAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adminTeacherAdapter.stopListening();
    }

    public void BackToAdminHomePage(View view) {
        Intent intent = new Intent(getApplicationContext(),AccountChooseActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }

    public void TeacherRegisterPage(View view) {
        Intent intent = new Intent(getApplicationContext(),AdminTeacherDetailsActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }
}