package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountChooseActivity extends AppCompatActivity {
    private AdminParentAdapter adminParentAdapter;
    private String sCode;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RecyclerView recyclerView;
    private AdminTeacherAdapter adminTeacherAdapter;
    private AdminAccountChooseAdapter adminAccountChooseAdapter;
    private FloatingActionButton teacher, parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_choose);

        Intent intent = getIntent();
        String Code = intent.getStringExtra("schoolCode");
        sCode = Code;
        recyclerView = (RecyclerView) findViewById(R.id.AdminTeacherRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teacher = findViewById(R.id.teacher);
        parent = findViewById(R.id.parent);
        parent.setVisibility(View.GONE);

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(AccountChooseActivity.this,TeacherRegisterActivity.class);
                intentHome.putExtra("schoolCode",sCode);
                startActivity(intentHome);
            }
        });

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(AccountChooseActivity.this,ParentRegisterActivity.class);
                intentHome.putExtra("schoolCode",sCode);
                startActivity(intentHome);
            }
        });

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        recyclerView = (RecyclerView) findViewById(R.id.AdminTeacherRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TeacherUserHelperClass> options =
                new FirebaseRecyclerOptions.Builder<TeacherUserHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("employee"),TeacherUserHelperClass.class)
                        .build();
        adminTeacherAdapter = new AdminTeacherAdapter(options);
        recyclerView.setAdapter(adminTeacherAdapter);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adminAccountChooseAdapter = new AdminAccountChooseAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(adminAccountChooseAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("Teacher"));
        tabLayout.addTab(tabLayout.newTab().setText("Parent"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                int position = tab.getPosition();
                if(position == 0){
                    teacher();
                    teacher.setVisibility(View.VISIBLE);
                    parent.setVisibility(View.GONE);
                }
                else{
                    parent();
                    teacher.setVisibility(View.GONE);
                    parent.setVisibility(View.VISIBLE);
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
        FirebaseRecyclerOptions<ParentHelperClass> options =
                new FirebaseRecyclerOptions.Builder<ParentHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("parent"),ParentHelperClass.class)
                        .build();
        adminParentAdapter = new AdminParentAdapter(options);
        recyclerView.setAdapter(adminParentAdapter);
        adminParentAdapter.startListening();
    }

    private void teacher() {
        FirebaseRecyclerOptions<TeacherUserHelperClass> options =
                new FirebaseRecyclerOptions.Builder<TeacherUserHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("employee"),TeacherUserHelperClass.class)
                        .build();
        adminTeacherAdapter = new AdminTeacherAdapter(options);
        recyclerView.setAdapter(adminTeacherAdapter);
        adminTeacherAdapter.startListening();
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


    public void chooseVerificationTochooseRegister(View view) {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }

//    public void teacherAccount(View view) {
//        Intent intentHome = new Intent(AccountChooseActivity.this,AdminTeacherDetailsActivity.class);
//        intentHome.putExtra("schoolCode",sCode);
//        startActivity(intentHome);
//    }
//
//    public void parentAccount(View view) {
//        Intent intentHome = new Intent(AccountChooseActivity.this,AdminParentDetailsActivity.class);
//        intentHome.putExtra("schoolCode",sCode);
//        startActivity(intentHome);
//    }
}