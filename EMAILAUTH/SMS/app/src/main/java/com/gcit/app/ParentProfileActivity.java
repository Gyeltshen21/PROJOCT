package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ParentProfileActivity extends AppCompatActivity {

    //private ImageView parentProfilePic;
    private TextView parentHeaderName, parentName, parentStdCode, parentEmail, parentPhoneNo;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        //parentProfilePic = (ImageView) findViewById(R.id.parentProfilePic);
        parentHeaderName = (TextView) findViewById(R.id.parentHeaderName1);
        parentName = (TextView) findViewById(R.id.parentName1);
        parentStdCode = (TextView) findViewById(R.id.parentStdCode1);
        parentEmail = (TextView) findViewById(R.id.parentEmail1);
        parentPhoneNo = (TextView) findViewById(R.id.parentPhoneNo1);
        Intent intent = getIntent();
        String stdCode = intent.getStringExtra("stdCode");
        sCode = stdCode;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("parent");
        Query checkUser = databaseReference.orderByChild("stdCode").equalTo(stdCode);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    parentStdCode.setError(null);
                    parentStdCode.setEnabled(false);
                    String nameDB = snapshot.child(stdCode).child("name").getValue(String.class);
                    String emailDB = snapshot.child(stdCode).child("email").getValue(String.class);
                    String phoneDB = snapshot.child(stdCode).child("phone").getValue(String.class);
                    String stdCodeDB = snapshot.child(stdCode).child("stdCode").getValue(String.class);
                    parentHeaderName.setText(nameDB);
                    parentName.setText(nameDB);
                    parentStdCode.setText(stdCodeDB);
                    parentEmail.setText(emailDB);
                    parentPhoneNo.setText(phoneDB);
                }
            }
            //Data fetch error
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Back button to home
    public void GoBackToParentHome(View view) {
        Intent intent = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        intent.putExtra("stdCode",sCode);
        startActivity(intent);
    }
}