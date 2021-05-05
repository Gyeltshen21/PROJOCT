package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParentPDFViewActivity extends AppCompatActivity {

    String s1;
    private ListView ParentPDFListView;
    DatabaseReference databaseReference;
    List<AdminPDFHelperClass> uploadPDFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_p_d_f_view);

        //Receive data from home activity
        Intent intent = getIntent();
        String stdCode = intent.getStringExtra("stdCode");
        s1 = stdCode;

        ParentPDFListView = (ListView) findViewById(R.id.ParentPDFListView);

        uploadPDFS = new ArrayList<>();

        //Call method
        viewTeacherAllPDFFiles();

        //Method to open pdf file

        ParentPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdminPDFHelperClass AdminPDFHelperClass = uploadPDFS.get(position);

                //Open pdf app in phone
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(AdminPDFHelperClass.getUrl()));
                startActivity(intent);
            }
        });
    }

    private void viewTeacherAllPDFFiles() {
        //Retrieve file from database
        databaseReference = FirebaseDatabase.getInstance().getReference("AdminResultPDF");
        Log.d("tag","data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    AdminPDFHelperClass AdminPDFHelperClass = postSnapshot.getValue(com.gcit.app.AdminPDFHelperClass.class);
                    //Adds all the pdf files
                    uploadPDFS.add(AdminPDFHelperClass);
                }
                String[] TeacherUploads = new String[uploadPDFS.size()];
                //Fetch all the files from storage
                for(int i = 0; i < TeacherUploads.length; i++){
                    TeacherUploads[i] = uploadPDFS.get(i).getName();
                }
                //Store in list view
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,TeacherUploads){
                    @Override
                    public View getView(int position,View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        TextView TeacherTextView = (TextView) view.findViewById(android.R.id.text1);
                        TeacherTextView.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                ParentPDFListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void BackToParentHome(View view) {
        Intent intent = new Intent(getApplicationContext(),ParentHomeActivity.class);
        intent.putExtra("stdCode",s1);
        startActivity(intent);
    }
}