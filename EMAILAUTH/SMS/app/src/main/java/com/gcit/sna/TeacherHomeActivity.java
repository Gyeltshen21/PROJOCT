package com.gcit.sna;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    private FirebaseAuth.AuthStateListener authStateListener;
    String s1;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        s1 = employeeID;
        drawerLayout = (DrawerLayout) findViewById(R.id.teacher_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.teacherinnav);
        toolbar = (Toolbar) findViewById(R.id.teacher_toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.teacher_navigation_drawer_open,R.string.teacher_navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.teacherinnav);
    }

    public void Notification(View view) {
        Intent homeIntent = new Intent(getApplicationContext(),TeacherNotificationActivity.class);
        homeIntent.putExtra("employeeID",s1);
        startActivity(homeIntent);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

    public void Result(View view) {
        Intent homeIntent = new Intent(getApplicationContext(),TeacherPDFActivity.class);
        homeIntent.putExtra("employeeID",s1);
        startActivity(homeIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.teacher_nav_profile:
                Intent homeIntent = new Intent(getApplicationContext(),TeacherProfileActivity.class);
                homeIntent.putExtra("employeeID",s1);
                startActivity(homeIntent);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                break;

            case R.id.teacher_nav_setting:
                Intent home = new Intent(getApplicationContext(),TeacherSettingActivity.class);
                home.putExtra("employeeID",s1);
                startActivity(home);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                break;

            case R.id.teacher_nav_about:
                Intent about = new Intent(getApplicationContext(),TeacherAboutActivity.class);
                about.putExtra("employeeID",s1);
                startActivity(about);
                about.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                break;

            case R.id.teacher_nav_logout:
                Intent intentHome = new Intent(TeacherHomeActivity.this,LoginActivity.class);
                startActivity(intentHome);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
        }
        return true;
    }
}