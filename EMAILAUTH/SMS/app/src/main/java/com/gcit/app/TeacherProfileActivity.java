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

public class TeacherProfileActivity extends AppCompatActivity {

    //private ImageView TeacherProfilePic;
    private TextView TeacherHeaderName, TeacherName, TeacherEmployeeID, TeacherEmail, TeacherPhoneNo;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        //TeacherProfilePic = (ImageView) findViewById(R.id.TeacherProfilePic);
        TeacherHeaderName = (TextView) findViewById(R.id.TeacherHeaderName1);
        TeacherName = (TextView) findViewById(R.id.TeacherName1);
        TeacherEmployeeID = (TextView) findViewById(R.id.TeacherEmployeeID1);
        TeacherEmail = (TextView) findViewById(R.id.TeacherEmail1);
        TeacherPhoneNo = (TextView) findViewById(R.id.TeacherPhoneNo1);
        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        sCode = employeeID;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("employee");
        Query checkUser = databaseReference.orderByChild("employeeID").equalTo(employeeID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TeacherEmployeeID.setError(null);
                    TeacherEmployeeID.setEnabled(false);
                    String nameDB = snapshot.child(employeeID).child("name").getValue(String.class);
                    String emailDB = snapshot.child(employeeID).child("email").getValue(String.class);
                    String phoneDB = snapshot.child(employeeID).child("phone").getValue(String.class);
                    String employeeIDDB = snapshot.child(employeeID).child("employeeID").getValue(String.class);
                    TeacherHeaderName.setText(nameDB);
                    TeacherName.setText(nameDB);
                    TeacherEmployeeID.setText(employeeIDDB);
                    TeacherEmail.setText(emailDB);
                    TeacherPhoneNo.setText(phoneDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoBackToTeacherHome(View view) {
        Intent intent = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        intent.putExtra("employeeID",sCode);
        startActivity(intent);
    }
}