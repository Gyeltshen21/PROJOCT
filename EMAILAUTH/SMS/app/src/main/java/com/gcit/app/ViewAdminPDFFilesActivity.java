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

public class ViewAdminPDFFilesActivity extends AppCompatActivity {

    String sCode;
    private ListView AdminPDFListView;
    DatabaseReference databaseReference;
    List<AdminPDFHelperClass> uploadPDFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_admin_p_d_f_files);

        //Receive data from home activity
        Intent intent = getIntent();
        String schoolCode = intent.getStringExtra("schoolCode");
        sCode = schoolCode;

        AdminPDFListView = (ListView) findViewById(R.id.AdminPDFListView);

        uploadPDFS = new ArrayList<>();
        
        //Call method
        viewAdminAllPDFFiles();

        //Method to open pdf file

        AdminPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdminPDFHelperClass adminPDFHelperClass = uploadPDFS.get(position);

                //Open pdf app in phone
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(adminPDFHelperClass.getUrl()));
                startActivity(intent);
            }
        });
    }

    private void viewAdminAllPDFFiles() {
        //Retrieve file from database
        databaseReference = FirebaseDatabase.getInstance().getReference("AdminResultPDF");
        Log.d("tag","data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    AdminPDFHelperClass adminPDFHelperClass = postSnapshot.getValue(com.gcit.app.AdminPDFHelperClass.class);
                    //Adds all the pdf files
                    uploadPDFS.add(adminPDFHelperClass);
                }
                String[] adminUploads = new String[uploadPDFS.size()];
                //Fetch all the files from storage
                for(int i = 0; i < adminUploads.length; i++){
                    adminUploads[i] = uploadPDFS.get(i).getName();
                }
                //Store in list view
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,adminUploads){
                    @Override
                    public View getView(int position,View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        TextView AdminTextView = (TextView) view.findViewById(android.R.id.text1);
                        AdminTextView.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                AdminPDFListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void BackToAdminPDFPage(View view) {
        Intent intent = new Intent(getApplicationContext(),AdminPDFActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }
}