package com.gcit.app;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout editTextName, editTextSchoolCode, editTextEmail, editTextPassword, editTextConfirmPassword;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
            String name = editTextName.getEditText().getText().toString().trim();
            String schoolCode = editTextSchoolCode.getEditText().getText().toString().trim();
            String email = editTextEmail.getEditText().getText().toString().trim();
            String password = editTextPassword.getEditText().getText().toString().trim();
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
            startActivity(registerIntent);
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
        else if(!PASSWORD_PATTERN.matcher(val).matches()){
            editTextPassword.setError("Password is too weak");
            editTextPassword.requestFocus();
            return false;
        }
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
        startActivity(registerIntent);
    }
    //Back button to login
    public void GoBackLoginPage(View view) {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
}