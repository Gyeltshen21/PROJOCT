package com.gcit.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class AdminParentDetailsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    AdminParentAdapter adminParentAdapter;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_parent_details);

        Intent intent = getIntent();
        String Code = intent.getStringExtra("schoolCode");
        sCode = Code;

        recyclerView = (RecyclerView) findViewById(R.id.AdminParentRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ParentHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ParentHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("parent"),ParentHelperClass.class)
                        .build();
        adminParentAdapter = new AdminParentAdapter(options);
        recyclerView.setAdapter(adminParentAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adminParentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adminParentAdapter.stopListening();
    }

    public void BackToChooseAccount(View view) {
        Intent intent = new Intent(getApplicationContext(),AdminParentDetailsActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }

    public void ParentRegisterPage(View view) {
        Intent intent = new Intent(getApplicationContext(),ParentRegisterActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }
}