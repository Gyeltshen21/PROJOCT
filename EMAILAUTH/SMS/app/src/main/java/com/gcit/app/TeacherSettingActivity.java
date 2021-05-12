package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class TeacherSettingActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private EditText teacherEditTextFullName, teacherEditTextEmail, teacherEditTextPhoneNo;
    private TextView teacherProfileUpdateSetting;
    String s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_setting);
        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        s1 = employeeID;
        teacherEditTextFullName = (EditText) findViewById(R.id.TeacherProfileName);
        teacherEditTextEmail = (EditText) findViewById(R.id.TeacherProfileEmail);
        teacherEditTextPhoneNo = (EditText) findViewById(R.id.TeacherProfilePhoneNo);
        teacherProfileUpdateSetting = (TextView) findViewById(R.id.TeacherUpdate);
    }
    //Update button
    public void TeacherProfileUpdate(View view) {
        if(!validateName() | !validateEmail() | !validatePhoneNumber()){
            return;
        }
        else{
            String name = teacherEditTextFullName.getText().toString().trim();
            String email = teacherEditTextEmail.getText().toString().trim();
            String phoneNo = teacherEditTextPhoneNo.getText().toString().trim();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("employee");
            Query checkUser = databaseReference.orderByChild("employeeID").equalTo(s1);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        databaseReference.child(s1).child("name").setValue(name);
                        databaseReference.child(s1).child("email").setValue(email);
                        databaseReference.child(s1).child("phone").setValue(phoneNo);
                        teacherEditTextFullName.setText("");
                        teacherEditTextEmail.setText("");
                        teacherEditTextPhoneNo.setText("");
                        teacherProfileUpdateSetting.setVisibility(View.VISIBLE);
                        teacherProfileUpdateSetting.setText("Profile Updated successfully");
                    }
                    else{
                        teacherProfileUpdateSetting.setVisibility(View.VISIBLE);
                        teacherProfileUpdateSetting.setText("Profile not Updated, Please try Again");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateName(){
        String val = teacherEditTextFullName.getText().toString().trim();
        if(val.isEmpty()){
            teacherEditTextFullName.setError("Full Name is Required!");
            teacherEditTextFullName.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validateEmail(){
        String val = teacherEditTextEmail.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty()){
            teacherEditTextEmail.setError("Email is Required!");
            teacherEditTextEmail.requestFocus();
            return false;
        }
        else if(!val.matches(checkEmail)){
            teacherEditTextEmail.setError("Please enter valid Email Address");
            teacherEditTextEmail.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validatePhoneNumber(){
        String val = teacherEditTextPhoneNo.getText().toString().trim();
        //final Pattern TPHONE_NUMBER = Pattern.compile("[7]{2}[0-9]{6}",Pattern.CASE_INSENSITIVE);
        final Pattern BPHONE_NUMBER = Pattern.compile("[1][7][0-9]{6}",Pattern.CASE_INSENSITIVE);
        if(val.isEmpty()){
            teacherEditTextPhoneNo.setError("Phone Number is Required!");
            teacherEditTextPhoneNo.requestFocus();
            return false;
        }
        else if(!BPHONE_NUMBER.matcher(val).matches()){
            teacherEditTextPhoneNo.setError("Invalid Phone Number");
            teacherEditTextPhoneNo.requestFocus();
            return false;
        }
        return true;
    }

    public void BackToTeacherHome(View view) {
        Intent intent = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        intent.putExtra("employeeID",s1);
        startActivity(intent);
    }
}