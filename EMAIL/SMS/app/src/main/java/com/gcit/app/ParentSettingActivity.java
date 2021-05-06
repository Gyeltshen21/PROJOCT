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

public class ParentSettingActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private EditText parentEditTextFullName, parentEditTextEmail, parentEditTextPhoneNo;
    private TextView parentProfileUpdateSetting;
    String s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_setting);
        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String stdCode = intent.getStringExtra("stdCode");
        s1 = stdCode;
        System.out.println("HELLO"+stdCode);
        parentEditTextFullName = (EditText) findViewById(R.id.ParentProfileName);
        parentEditTextEmail = (EditText) findViewById(R.id.ParentProfileEmail);
        parentEditTextPhoneNo = (EditText) findViewById(R.id.ParentProfilePhoneNo);
        parentProfileUpdateSetting = (TextView) findViewById(R.id.ParentUpdate);
    }
    //Update button
    public void ParentProfileUpdate(View view) {
        if(!validateName() | !validateEmail() | !validatePhoneNumber()){
            return;
        }
        else{
            String name = parentEditTextFullName.getText().toString().trim();
            String email = parentEditTextEmail.getText().toString().trim();
            String phoneNo = parentEditTextPhoneNo.getText().toString().trim();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("parent");
            Query checkUser = databaseReference.orderByChild("stdCode").equalTo(s1);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        databaseReference.child(s1).child("name").setValue(name);
                        databaseReference.child(s1).child("email").setValue(email);
                        databaseReference.child(s1).child("phone").setValue(phoneNo);
                        parentEditTextFullName.setText("");
                        parentEditTextEmail.setText("");
                        parentEditTextPhoneNo.setText("");
                        parentProfileUpdateSetting.setVisibility(View.VISIBLE);
                        parentProfileUpdateSetting.setText("Profile Updated successfully");
                    }
                    else{
                        parentProfileUpdateSetting.setVisibility(View.VISIBLE);
                        parentProfileUpdateSetting.setText("Profile Not Updated, Please try Again");
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
        String val = parentEditTextFullName.getText().toString().trim();
        if(val.isEmpty()){
            parentEditTextFullName.setError("Full Name is Required!");
            parentEditTextFullName.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validateEmail(){
        String val = parentEditTextEmail.getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty()){
            parentEditTextEmail.setError("Email is Required!");
            parentEditTextEmail.requestFocus();
            return false;
        }
        else if(!val.matches(checkEmail)){
            parentEditTextEmail.setError("Please enter valid Email Address");
            parentEditTextEmail.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validatePhoneNumber(){
        String val = parentEditTextPhoneNo.getText().toString().trim();
        String checksTNumber = "(0/91)?[7][7][0-9]{6}";
        String checksBNumber = "(0/91)?[1][7][0-9]{6}";
        if(val.isEmpty()){
            parentEditTextPhoneNo.setError("Phone Number is Required!");
            parentEditTextPhoneNo.requestFocus();
            return false;
        }
        else if(!val.matches(checksTNumber)){
            parentEditTextPhoneNo.setError("Invalid Phone Number");
            parentEditTextPhoneNo.requestFocus();
            return false;
        }
        return true;
    }

    public void BackToParentHome(View view) {
        Intent intent = new Intent(getApplicationContext(),ParentHomeActivity.class);
        intent.putExtra("stdCode",s1);
        startActivity(intent);
    }
}