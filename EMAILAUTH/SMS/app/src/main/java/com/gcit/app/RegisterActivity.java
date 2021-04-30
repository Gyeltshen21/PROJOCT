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
    TextInputLayout editTextName, editTextSchoolCode, editTextEmail, editTextPhoneNo, editTextPassword, editTextConfirmPassword;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    final loadingDailogue dailogue = new loadingDailogue(RegisterActivity.this);
    String phoneNo, name, schoolCode, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextName = (TextInputLayout) findViewById(R.id.name);
        editTextSchoolCode = (TextInputLayout) findViewById(R.id.schoolcode);
        editTextEmail = (TextInputLayout) findViewById(R.id.email);
        editTextPhoneNo = (TextInputLayout) findViewById(R.id.phoneNo);
        editTextPassword = (TextInputLayout) findViewById(R.id.password);
        editTextConfirmPassword = (TextInputLayout) findViewById(R.id.confirmpassword);
    }

    public void callVerifyScreen (View view){
        dailogue.startLoadingDialogue();
        if (!validateName() | !validateSchoolCode() | !validateEmail() | !validatePhoneNumber() | !validatePassword()) {
            dailogue.dismiss();
            return;
        }
        else {
            String name = editTextName.getEditText().getText().toString().trim();
            String schoolCode = editTextSchoolCode.getEditText().getText().toString().trim();
            String email = editTextEmail.getEditText().getText().toString().trim();
            String phoneNo = editTextPhoneNo.getEditText().getText().toString().trim();
            String password = editTextPassword.getEditText().getText().toString().trim();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task)-> {
                if (!task.isSuccessful()) {
                    dailogue.dismiss();
                    Toast.makeText(RegisterActivity.this, "Account not Registered, Please check your Details", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(RegisterActivity.this, "Verification link has been sent to your Email, Please check your Email", Toast.LENGTH_SHORT).show();
                            Intent registerIntent = new Intent(getApplicationContext(), VerifyEmailActivity.class);
                            registerIntent.putExtra("admin_name",name);
                            registerIntent.putExtra("sCode",schoolCode);
                            registerIntent.putExtra("email",email);
                            registerIntent.putExtra("phoneNo",phoneNo);
                            registerIntent.putExtra("password",password);
                            editTextName.getEditText().setText("");
                            editTextSchoolCode.getEditText().setText("");
                            editTextEmail.getEditText().setText("");
                            editTextPhoneNo.getEditText().setText("");
                            editTextPassword.getEditText().setText("");
                            editTextConfirmPassword.getEditText().setText("");
                            startActivity(registerIntent);
                        }
                    });
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
        String checksTNumber = "(0/91)?[7][7][0-9]{6}";
        String checksBNumber = "(0/91)?[1][7][0-9]{6}";
        if(val.isEmpty()){
            editTextPhoneNo.setError("Phone Number is Required!");
            editTextPhoneNo.requestFocus();
            return false;
        }
        else if(!val.matches(checksTNumber)){
            editTextPhoneNo.setError("Invalid Phone Number");
            editTextPhoneNo.requestFocus();
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
    public void callLoginPage(View view) {
        dailogue.startLoadingDialogue();
        Intent registerIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(registerIntent);
    }
}