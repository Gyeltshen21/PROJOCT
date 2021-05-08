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

public class AdminNotificationActivity extends AppCompatActivity {

    FloatingActionButton AdminFloatingBtn;
    private RecyclerView recyclerView;
    private AdminNotificationAdapter adminNotificationAdapter;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);

        Intent intent = getIntent();
        String Code = intent.getStringExtra("schoolCode");
        sCode = Code;

        AdminFloatingBtn = (FloatingActionButton) findViewById(R.id.AdminNotificationFloatingBar);
        AdminFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminNotificationAddActivity.class);
                intent.putExtra("schoolCode",sCode);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.AdminNotificationRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<AdminNotificationHelperClass> options =
                new FirebaseRecyclerOptions.Builder<AdminNotificationHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("AdminNotification"),AdminNotificationHelperClass.class)
                        .build();
        adminNotificationAdapter = new AdminNotificationAdapter(options);
        recyclerView.setAdapter(adminNotificationAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adminNotificationAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adminNotificationAdapter.stopListening();
    }
    public void BackToAdminNotificationHome(View view) {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }
}