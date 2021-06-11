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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ParentHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private androidx.appcompat.widget.Toolbar toolbar;
    String s1;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        Intent intent = getIntent();
        String stdCode = intent.getStringExtra("stdCode");
        s1 = stdCode;
        drawerLayout = (DrawerLayout) findViewById(R.id.parent_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.parentinnav);
        toolbar = (Toolbar) findViewById(R.id.parent_toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.parent_navigation_drawer_open,R.string.parent_navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.parentinnav);
    }

    public void Notification(View view) {
        Intent homeIntent = new Intent(getApplicationContext(),ParentNotificationActivity.class);
        homeIntent.putExtra("stdCode",s1);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
        finish();
    }

    public void Result(View view) {
        Intent homeIntent = new Intent(getApplicationContext(),ParentPDFActivity.class);
        homeIntent.putExtra("stdCode",s1);
        startActivity(homeIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.parent_nav_profile:
                Intent homeIntent = new Intent(getApplicationContext(),ParentProfileActivity.class);
                homeIntent.putExtra("stdCode",s1);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
                break;

            case R.id.parent_nav_setting:
                Intent home = new Intent(getApplicationContext(),ParentSettingActivity.class);
                home.putExtra("stdCode",s1);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(home);
                finish();
                break;

            case R.id.parent_nav_about:
                Intent about = new Intent(getApplicationContext(),ParentAboutActivity.class);
                about.putExtra("stdCode",s1);
                about.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(about);
                finish();
                break;

            case R.id.parent_nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intentHome = new Intent(ParentHomeActivity.this,LoginActivity.class);
                startActivity(intentHome);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
        }
        return true;
    }
}