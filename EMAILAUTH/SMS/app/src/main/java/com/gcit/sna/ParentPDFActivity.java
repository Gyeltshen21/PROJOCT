package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ParentPDFActivity extends AppCompatActivity {

    RecyclerView ParentRecycleView;
    ParentPDFAdapter ParentPDFAdapter;
    String s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_p_d_f);

        Intent intent = getIntent();
        String stdCode = intent.getStringExtra("stdCode");
        s1 = stdCode;

        ParentRecycleView = (RecyclerView) findViewById(R.id.ParentPDFRecycleView);
        ParentRecycleView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TeacherPDFHelperClass> options =
                new FirebaseRecyclerOptions.Builder<TeacherPDFHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TeacherPDFResult"),TeacherPDFHelperClass.class)
                        .build();

        ParentPDFAdapter = new ParentPDFAdapter(options);
        ParentRecycleView.setAdapter(ParentPDFAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ParentPDFAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ParentPDFAdapter.stopListening();
    }

    public void BackToParentHome(View view) {
        Intent intentHome = new Intent(getApplicationContext(),ParentHomeActivity.class);
        intentHome.putExtra("stdCode",s1);
        startActivity(intentHome);
    }
}