package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class TeacherAboutActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private PagerAdapter pagerAdapter;
    String s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_about);
        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        s1 = employeeID;
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

    public void callBackTeacherHome(View view) {
        Intent password = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        password.putExtra("employeeID",s1);
        startActivity(password);
    }
}