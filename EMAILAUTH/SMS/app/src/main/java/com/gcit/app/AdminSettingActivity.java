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

public class AdminSettingActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private EditText adminEditTextFullName, adminEditTextEmail, adminEditTextPhoneNo;
    private TextView adminProfileUpdateSetting;
    String s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setting);
        firebaseAuth = FirebaseAuth.getInstance();

        String sCode = getIntent().getStringExtra("schoolCode");
        s1 = sCode;
        adminEditTextFullName = (EditText) findViewById(R.id.AdminProfileName);
        adminEditTextEmail = (EditText) findViewById(R.id.AdminProfileEmail);
        adminEditTextPhoneNo = (EditText) findViewById(R.id.AdminProfilePhoneNo);
        adminProfileUpdateSetting = (TextView) findViewById(R.id.AdminUpdate);
    }
    //Update button
    public void adminProfileUpdate(View view) {
        if(!validateName() | !validateEmail() | !validatePhoneNumber()){
            return;
        }
        else{
            String name = adminEditTextFullName.getText().toString().trim();
            String email = adminEditTextEmail.getText().toString().trim();
            String phoneNo = adminEditTextPhoneNo.getText().toString().trim();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
            Query checkUser = databaseReference.orderByChild("schoolCode").equalTo(s1);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        databaseReference.child(s1).child("name").setValue(name);
                        databaseReference.child(s1).child("email").setValue(email);
                        databaseReference.child(s1).child("phone").setValue(phoneNo);
                        adminEditTextFullName.setText("");
                        adminEditTextEmail.setText("");
                        adminEditTextPhoneNo.setText("");
                        adminProfileUpdateSetting.setVisibility(View.VISIBLE);
                        adminProfileUpdateSetting.setText("Profile Updated successfully");
                    }
                    else{
                        adminProfileUpdateSetting.setVisibility(View.VISIBLE);
                        adminProfileUpdateSetting.setText("Profile Not Updated, Please try Again");
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
        String val = adminEditTextFullName.getText().toString().trim();
        if(val.isEmpty()){
            adminEditTextFullName.setError("Full Name is Required!");
            adminEditTextFullName.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validateEmail(){
        String val = adminEditTextEmail.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty()){
            adminEditTextEmail.setError("Email is Required!");
            adminEditTextEmail.requestFocus();
            return false;
        }
        else if(!val.matches(checkEmail)){
            adminEditTextEmail.setError("Please enter valid Email Address");
            adminEditTextEmail.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validatePhoneNumber() {
        String val = adminEditTextPhoneNo.getText().toString().trim();
        //final Pattern TPHONE_NUMBER = Pattern.compile("[7]{2}[0-9]{6}",Pattern.CASE_INSENSITIVE);
        final Pattern BPHONE_NUMBER = Pattern.compile("[+][9][7][5][1][7][0-9]{6}", Pattern.CASE_INSENSITIVE);
        if (val.isEmpty()) {
            adminEditTextPhoneNo.setError("Phone Number is Required!");
            adminEditTextPhoneNo.requestFocus();
            return false;
        } else if (!BPHONE_NUMBER.matcher(val).matches()) {
            adminEditTextPhoneNo.setError("Invalid Phone Number");
            adminEditTextPhoneNo.requestFocus();
            return false;
        }
        return true;
    }
    public void BackToHome(View view) {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("schoolCode",s1);
        startActivity(intent);
    }
}