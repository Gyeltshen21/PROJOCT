package com.gcit.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ParentNotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParentNotificationAdapter parentNotificationAdapter;
    String s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_notification);

        Intent intent = getIntent();
        String stdCode = intent.getStringExtra("stdCode");
        s1 = stdCode;

        recyclerView = (RecyclerView) findViewById(R.id.ParentNotificationRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<AdminNotificationHelperClass> options =
                new FirebaseRecyclerOptions.Builder<AdminNotificationHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("AdminNotification"),AdminNotificationHelperClass.class)
                        .build();
        parentNotificationAdapter = new ParentNotificationAdapter(options);
        recyclerView.setAdapter(parentNotificationAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        parentNotificationAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        parentNotificationAdapter.stopListening();
    }
    public void BackToParentNotificationHome(View view) {
        Intent intent = new Intent(getApplicationContext(),ParentHomeActivity.class);
        intent.putExtra("stdCode",s1);
        startActivity(intent);
    }
}