package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    Menu menu;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    String s1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        String sCode1 = intent.getStringExtra("schoolCode");
        s1 = sCode1;

        drawerLayout = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.adminnav);
        toolbar = (Toolbar) findViewById(R.id.admin_toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.admin_navigation_drawer_open,R.string.admin_navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.adminnav);
    }

    public void Notification(View view) {
        Toast.makeText(HomeActivity.this, "Notification Clicked", Toast.LENGTH_SHORT).show();
    }

    public void Result(View view) {
        Toast.makeText(HomeActivity.this, "Result Clicked", Toast.LENGTH_SHORT).show();
    }

    public void CreateAccount(View view) {
        Intent intentHome = new Intent(HomeActivity.this,AccountChooseActivity.class);
        intentHome.putExtra("schoolCode",s1);
        startActivity(intentHome);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.admin_nav_profile:
                Intent homeIntent = new Intent(this,AdminProfileActivity.class);
                homeIntent.putExtra("schoolCode",s1);
                startActivity(homeIntent);
                break;

            case R.id.admin_nav_setting:
                Intent home = new Intent(this,AdminSettingActivity.class);
                home.putExtra("schoolCode",s1);
                startActivity(home);
                break;

            case R.id.admin_nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intentHome = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intentHome);
                finish();
                break;
        }
            return true;
    }
}