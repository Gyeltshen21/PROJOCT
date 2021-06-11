package com.gcit.sna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout editTextName, editTextSchoolCode, editTextEmail, editTextPassword, editTextConfirmPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Creates Instance FirebaseDatabase
        reference = FirebaseDatabase.getInstance().getReference("SCHOOLCODE");

        firebaseAuth = FirebaseAuth.getInstance();
        editTextName = (TextInputLayout) findViewById(R.id.name);
        editTextSchoolCode = (TextInputLayout) findViewById(R.id.schoolcode);
        editTextEmail = (TextInputLayout) findViewById(R.id.email);
        editTextPassword = (TextInputLayout) findViewById(R.id.password);
        editTextConfirmPassword = (TextInputLayout) findViewById(R.id.confirmpassword);
    }
    //Button to register
    public void callVerifyScreen (View view){
        if (!validateName() | !validateSchoolCode() | !validateEmail() | !validatePassword()) {
            return;
        }
        else {
            String schoolCode = editTextSchoolCode.getEditText().getText().toString().trim();
            String name = editTextName.getEditText().getText().toString().trim();
            String email = editTextEmail.getEditText().getText().toString().trim();
            String password = editTextPassword.getEditText().getText().toString().trim();
            Query checkUser = reference.orderByChild("code").equalTo(schoolCode);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        editTextSchoolCode.setError(null);
                        editTextSchoolCode.setEnabled(true);

                        //Checks weather school code is register to our app
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                        Query checkCode = databaseReference.orderByChild("schoolCode").equalTo(schoolCode);
                        checkCode.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    editTextSchoolCode.setError("This code has been already used, Please try with different code");
                                    editTextSchoolCode.requestFocus();
                                    return;
                                }
                                else{
                                    Intent registerIntent = new Intent(RegisterActivity.this, AdminPhoneAuthActivity.class);
                                    registerIntent.putExtra("name",name);
                                    registerIntent.putExtra("schoolCode",schoolCode);
                                    registerIntent.putExtra("email",email);
                                    registerIntent.putExtra("password",password);
                                    editTextName.getEditText().setText("");
                                    editTextSchoolCode.getEditText().setText("");
                                    editTextEmail.getEditText().setText("");
                                    editTextPassword.getEditText().setText("");
                                    editTextConfirmPassword.getEditText().setText("");
                                    registerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(registerIntent);
                                    finish();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    else{
                        editTextSchoolCode.setError("No such school code is in Bhutan schools");
                        editTextSchoolCode.requestFocus();
                        return;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateName(){
        String val = editTextName.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            editTextName.setError("Full Name is Required!");
            editTextName.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validateSchoolCode(){
        String val = editTextSchoolCode.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            editTextSchoolCode.setError("School Code is Required!");
            editTextSchoolCode.requestFocus();
            return false;
        }
        else if(val.length() != 3) {
            editTextSchoolCode.setError("School Code should be exactly 3");
            editTextSchoolCode.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateEmail(){
        String val = editTextEmail.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty()){
            editTextEmail.setError("Email is Required!");
            editTextEmail.requestFocus();
            return false;
        }
        else if(!val.matches(checkEmail)){
            editTextEmail.setError("Please enter valid Email Address");
            editTextEmail.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validatePassword(){
        String val = editTextPassword.getEditText().getText().toString().trim();
        String val1 = editTextConfirmPassword.getEditText().getText().toString().trim();
        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[@#$%^&+=])" +     // at least 1 special character
                        "(?=\\S+$)" +            // no white spaces
                        ".{4,}" +                // at least 4 characters
                        "$");
        if(val.isEmpty()){
            editTextPassword.setError("Password is Empty");
            editTextPassword.requestFocus();
            return false;
        }
        else if(val1.isEmpty()){
            editTextConfirmPassword.setError("Confirm Password is Empty");
            editTextConfirmPassword.requestFocus();
            return false;
        }
        else if(val.length() < 8) {
            editTextPassword.setError("Password is too short, it should be at least 8");
            editTextPassword.requestFocus();
            return false;
        }
//        else if(!PASSWORD_PATTERN.matcher(val).matches()){
//            editTextPassword.setError("Password is too weak");
//            editTextPassword.requestFocus();
//            return false;
//        }
        else if(!val.equals(val1)){
            editTextConfirmPassword.setError("Confirm Password is didn't match");
            editTextConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
    //Button to register
    public void callLoginPage(View view) {
        Intent registerIntent = new Intent(getApplicationContext(), LoginActivity.class);
        registerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(registerIntent);
        finish();
    }
    //Back button to login
    public void GoBackLoginPage(View view) {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}