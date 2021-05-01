package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminProfileActivity extends AppCompatActivity {

    //private ImageView AdminProfilePic;
    private TextView AdminHeaderName, AdminName, AdminSchoolCode, AdminEmail, AdminPhoneNo;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        //AdminProfilePic = (ImageView) findViewById(R.id.AdminProfilePic);
        AdminHeaderName = (TextView) findViewById(R.id.AdminHeaderName);
        AdminName = (TextView) findViewById(R.id.AdminName);
        AdminSchoolCode = (TextView) findViewById(R.id.AdminCode);
        AdminEmail = (TextView) findViewById(R.id.AdminEmail);
        AdminPhoneNo = (TextView) findViewById(R.id.AdminPhoneNo);
        Intent intent = getIntent();
        String schoolCode = intent.getStringExtra("schoolCode");
        sCode = schoolCode;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = databaseReference.orderByChild("schoolCode").equalTo(sCode);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    AdminSchoolCode.setError(null);
                    AdminSchoolCode.setEnabled(false);
                    String nameDB = snapshot.child(schoolCode).child("name").getValue(String.class);
                    String emailDB = snapshot.child(schoolCode).child("email").getValue(String.class);
                    String phoneDB = snapshot.child(schoolCode).child("phone").getValue(String.class);
                    String schoolCodeDB = snapshot.child(schoolCode).child("schoolCode").getValue(String.class);
                    AdminHeaderName.setText(nameDB);
                    AdminName.setText(nameDB);
                    AdminSchoolCode.setText(schoolCodeDB);
                    AdminEmail.setText(emailDB);
                    AdminPhoneNo.setText(phoneDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void GoBackToHome(View view) {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }
}