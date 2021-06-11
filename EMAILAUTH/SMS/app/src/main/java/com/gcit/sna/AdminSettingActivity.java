package com.gcit.sna;

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
    private FirebaseAuth firebaseAuth;
    private EditText adminEditTextFullName, adminEditTextEmail, adminEditTextPhoneNo;
    String s1, password;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setting);
        firebaseAuth = FirebaseAuth.getInstance();

        String sCode = getIntent().getStringExtra("schoolCode");
        s1 = sCode;
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        adminEditTextFullName = (EditText) findViewById(R.id.AdminProfileName);
        adminEditTextEmail = (EditText) findViewById(R.id.AdminProfileEmail);
        adminEditTextPhoneNo = (EditText) findViewById(R.id.AdminProfilePhoneNo);

        //Set profile existing details
        Query checkUser = databaseReference.orderByChild("schoolCode").equalTo(s1);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nameDB = snapshot.child(sCode).child("name").getValue(String.class);
                    String emailDB = snapshot.child(sCode).child("email").getValue(String.class);
                    String phoneDB = snapshot.child(sCode).child("phone").getValue(String.class);
                    String passwordDB = snapshot.child(sCode).child("password").getValue(String.class);
                    password = passwordDB;
                    adminEditTextFullName.setText(nameDB);
                    adminEditTextEmail.setText(emailDB);
                    adminEditTextPhoneNo.setText(phoneDB);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Update button
    public void adminProfileUpdate(View view) {
        if(!validateName() | !validateEmail() | !validatePhoneNumber()){
            return;
        }
        else{
            String name = adminEditTextFullName.getText().toString().trim();
            String email = adminEditTextEmail.getText().toString().trim();
            String number = adminEditTextPhoneNo.getText().toString().trim();
            Intent intent = new Intent(AdminSettingActivity.this, SettingUpdateActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("phone", number);
            intent.putExtra("name", name);
            intent.putExtra("password", password);
            intent.putExtra("schoolCode", s1);
            intent.putExtra("whatToDo", "AdminSettingUpdate");
            startActivity(intent);
            adminEditTextFullName.setText("");
            adminEditTextEmail.setText("");
            adminEditTextPhoneNo.setText("");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void adminChangePassword(View view) {
        Intent password = new Intent(this,AdminChangePasswordActivity.class);
        password.putExtra("schoolCode",s1);
        password.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(password);
        finish();
    }
}