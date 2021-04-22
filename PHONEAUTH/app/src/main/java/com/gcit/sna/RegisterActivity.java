package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout editTextSchoolCode, editTextEmail, editTextPassword, editTextConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextSchoolCode = (TextInputLayout) findViewById(R.id.schoolcode);
        editTextEmail = (TextInputLayout) findViewById(R.id.email);
        editTextPassword = (TextInputLayout) findViewById(R.id.password);
        editTextConfirmPassword = (TextInputLayout) findViewById(R.id.confirmpassword);
    }

    public void callPhoneNoScreen (View view){
        if (!validateSchoolCode() | !validateEmail() | !validatePassword()) {
            return;
        } else {
            String schoolCode = editTextSchoolCode.getEditText().getText().toString().trim();
            String email = editTextEmail.getEditText().getText().toString().trim();
            String password = editTextPassword.getEditText().getText().toString().trim();

            Intent registerIntent = new Intent(getApplicationContext(), PhoneNoActivity.class);
            registerIntent.putExtra("SCHOOLCODE", schoolCode);
            registerIntent.putExtra("EMAIL", email);
            registerIntent.putExtra("PASSWORD", password);
            startActivity(registerIntent);
        }
    }
    private boolean validateSchoolCode(){
        String val = editTextSchoolCode.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            editTextSchoolCode.setError("School Code is Required!");
            editTextSchoolCode.requestFocus();
            return false;
        }
        else if(val.length() > 10) {
            editTextSchoolCode.setError("School Code is too large, it should less then 10");
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
        if(val.isEmpty()){
            editTextPassword.setError("Password is Empty");
            editTextPassword.requestFocus();
        }
        else if(val.length() < 6) {
            editTextPassword.setError("Password is too short, it should be atleast 6");
            editTextPassword.requestFocus();
            return false;
        }
        return true;
    }
}