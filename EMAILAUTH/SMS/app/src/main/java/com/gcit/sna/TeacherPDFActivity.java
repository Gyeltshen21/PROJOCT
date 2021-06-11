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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherPDFActivity extends AppCompatActivity {
    private RecyclerView TeacherRecycleView;
    private TeacherPDFAdapter teacherPDFAdapter;
    private FloatingActionButton TeacherFloatingBar;

    private Toolbar toolbar;

    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_p_d_f);

        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        sCode = employeeID;

        toolbar = findViewById(R.id.teacher_toolbar);
        setSupportActionBar(toolbar);

        TeacherFloatingBar = (FloatingActionButton)findViewById(R.id.TeacherFloatingBar);
        TeacherFloatingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TeacherUploadActivity.class);
                intent.putExtra("employeeID",sCode);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
            }
        });

        TeacherRecycleView = (RecyclerView) findViewById(R.id.TeacherPDFRecycleView);
        TeacherRecycleView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TeacherPDFHelperClass> options =
                new FirebaseRecyclerOptions.Builder<TeacherPDFHelperClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("TeacherPDFResult"),TeacherPDFHelperClass.class)
                .build();

        teacherPDFAdapter = new TeacherPDFAdapter(options);
        TeacherRecycleView.setAdapter(teacherPDFAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        teacherPDFAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        teacherPDFAdapter.stopListening();
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
        teacherPDFAdapter = new TeacherPDFAdapter(options);
        teacherPDFAdapter.startListening();
        TeacherRecycleView.setAdapter(teacherPDFAdapter);
    }

    public void BackToTeacherHome(View view) {
        Intent intent = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        intent.putExtra("employeeID",sCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}