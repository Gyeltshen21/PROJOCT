package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ParentPDFActivity extends AppCompatActivity {

    private RecyclerView ParentRecycleView;
    private ParentPDFAdapter ParentPDFAdapter;
    String s1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_p_d_f);

        Intent intent = getIntent();
        String stdCode = intent.getStringExtra("stdCode");
        s1 = stdCode;
        toolbar = findViewById(R.id.Parent_toolbar);
        setSupportActionBar(toolbar);

        ParentRecycleView = (RecyclerView) findViewById(R.id.ParentPDFRecycleView);
        ParentRecycleView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TeacherPDFHelperClass> options =
                new FirebaseRecyclerOptions.Builder<TeacherPDFHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TeacherPDFResult"),TeacherPDFHelperClass.class)
                        .build();

        ParentPDFAdapter = new ParentPDFAdapter(options);
        ParentRecycleView.setAdapter(ParentPDFAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ParentPDFAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ParentPDFAdapter.stopListening();
    }

    //Search toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_search_menu,menu);
        MenuItem item = menu.findItem(R.id.admin_search);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processSearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    //Generate request
    private void processSearch(String s) {
        FirebaseRecyclerOptions<TeacherPDFHelperClass> options =
                new FirebaseRecyclerOptions.Builder<TeacherPDFHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("TeacherPDFResult").orderByChild("name").startAt(s).endAt(s+"\uf8ff"),TeacherPDFHelperClass.class)
                        .build();
        ParentPDFAdapter = new ParentPDFAdapter(options);
        ParentPDFAdapter.startListening();
        ParentRecycleView.setAdapter(ParentPDFAdapter);
    }

    public void BackToAdminHome(View view) {
        Intent intentHome = new Intent(getApplicationContext(),HomeActivity.class);
        intentHome.putExtra("schoolCode",s1);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentHome);
        finish();
    }

    public void BackToParentHome(View view) {
        Intent intentHome = new Intent(getApplicationContext(),ParentHomeActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentHome);
        finish();
    }
}