package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class AdminAboutActivity extends AppCompatActivity {
    String sCode;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_about);
        Intent intent = getIntent();
        String Code = intent.getStringExtra("schoolCode");
        sCode = Code;

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        pagerAdapter = new PagerAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("About US"));
        tabLayout.addTab(tabLayout.newTab().setText("About this App"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    public void callBackHome(View view) {
        Intent intentHome = new Intent(this,HomeActivity.class);
        intentHome.putExtra("schoolCode",sCode);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentHome);
        finish();
    }
}