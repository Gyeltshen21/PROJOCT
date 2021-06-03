package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNotificationActivity extends AppCompatActivity {

    FloatingActionButton AdminFloatingBtn;
    private RecyclerView recyclerView;
    private AdminNotificationAdapter adminNotificationAdapter;
    private AdminNotificationParentAdapter adminNotificationParentAdapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private AdminNotificationFragmentAdapter adminNotificationFragmentAdapter;
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

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        recyclerView = (RecyclerView) findViewById(R.id.AdminNotificationRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<AdminNotificationHelperClass> options =
                new FirebaseRecyclerOptions.Builder<AdminNotificationHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TeacherNews"),AdminNotificationHelperClass.class)
                        .build();
        adminNotificationAdapter = new AdminNotificationAdapter(options);
        recyclerView.setAdapter(adminNotificationAdapter);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adminNotificationFragmentAdapter = new AdminNotificationFragmentAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(adminNotificationFragmentAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("Teacher"));
        tabLayout.addTab(tabLayout.newTab().setText("Parent"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                int position = tab.getPosition();
                if(position == 0){
                    teacher();
                }
                else{
                    parent();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void parent() {
        FirebaseRecyclerOptions<AdminNotificationHelperClass> options =
                new FirebaseRecyclerOptions.Builder<AdminNotificationHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("ParentNews"),AdminNotificationHelperClass.class)
                        .build();
        adminNotificationParentAdapter = new AdminNotificationParentAdapter(options);
        recyclerView.setAdapter(adminNotificationParentAdapter);
        adminNotificationParentAdapter.startListening();
    }

    private void teacher() {
        FirebaseRecyclerOptions<AdminNotificationHelperClass> options =
                new FirebaseRecyclerOptions.Builder<AdminNotificationHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TeacherNews"),AdminNotificationHelperClass.class)
                        .build();
        adminNotificationAdapter = new AdminNotificationAdapter(options);
        recyclerView.setAdapter(adminNotificationAdapter);
        adminNotificationAdapter.startListening();
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