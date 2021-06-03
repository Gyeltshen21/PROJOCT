package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class AdminPDFActivity extends AppCompatActivity {

    RecyclerView AdminRecycleView;
    AdminPDFAdapter AdminPDFAdapter;
    String s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_p_d_f);

        Intent intent = getIntent();
        String sCode1 = intent.getStringExtra("schoolCode");
        s1 = sCode1;

        AdminRecycleView = (RecyclerView) findViewById(R.id.AdminPDFRecycleView);
        AdminRecycleView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TeacherPDFHelperClass> options =
                new FirebaseRecyclerOptions.Builder<TeacherPDFHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TeacherPDFResult"),TeacherPDFHelperClass.class)
                        .build();

        AdminPDFAdapter = new AdminPDFAdapter(options);
        AdminRecycleView.setAdapter(AdminPDFAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AdminPDFAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AdminPDFAdapter.stopListening();
    }

    public void BackToAdminHome(View view) {
        Intent intentHome = new Intent(getApplicationContext(),HomeActivity.class);
        intentHome.putExtra("schoolCode",s1);
        startActivity(intentHome);
    }
}