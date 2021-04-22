package com.gcit.app;

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

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout editTextSchoolCode, editTextEmail, editTextPhoneNo, editTextPassword, editTextConfirmPassword;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String phoneNo, schoolCode, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        editTextSchoolCode = (TextInputLayout) findViewById(R.id.schoolcode);
        editTextEmail = (TextInputLayout) findViewById(R.id.email);
        editTextPhoneNo = (TextInputLayout) findViewById(R.id.phoneNo);
        editTextPassword = (TextInputLayout) findViewById(R.id.password);
        editTextConfirmPassword = (TextInputLayout) findViewById(R.id.confirmpassword);
    }

    public void callVerifyScreen (View view){
        if (!validateSchoolCode() | !validateEmail() | !validatePhoneNumber() | !validatePassword()) {
            return;
        }
        else {
            String schoolCode = editTextSchoolCode.getEditText().getText().toString().trim();
            String email = editTextEmail.getEditText().getText().toString().trim();
            String phoneNo = editTextPhoneNo.getEditText().getText().toString().trim();
            String password = editTextPassword.getEditText().getText().toString().trim();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task)-> {
                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Account not Registered, Please check your Details", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(RegisterActivity.this, "Verification link has been sent to your Email, Please check your Email", Toast.LENGTH_SHORT).show();
                            Intent registerIntent = new Intent(getApplicationContext(), VerifyEmailActivity.class);
                            registerIntent.putExtra("sCode",schoolCode);
                            registerIntent.putExtra("email",email);
                            registerIntent.putExtra("phoneNo",phoneNo);
                            registerIntent.putExtra("password",password);
                            startActivity(registerIntent);
                        }
                    });
                }
            });
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
    private boolean validatePhoneNumber(){
        String val = editTextPhoneNo.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            editTextPhoneNo.setError("Phone Number is Required!");
            editTextPhoneNo.requestFocus();
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

    public void callLoginPage(View view) {
        Intent registerIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(registerIntent);
    }
}