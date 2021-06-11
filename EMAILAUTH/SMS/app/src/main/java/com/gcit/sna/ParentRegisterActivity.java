package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class ParentRegisterActivity extends AppCompatActivity {

    TextInputLayout parentFullName, parentstdCode, parentEmail, parentPhoneNo, parentPassword, parentConfirmPassword;
    androidx.appcompat.widget.Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String phoneNo, stdCode, email, password, fullname, s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);
        firebaseAuth = FirebaseAuth.getInstance();

        String sCode = getIntent().getStringExtra("schoolCode");
        s2 = sCode;

        parentFullName = (TextInputLayout) findViewById(R.id.ParentName);
        parentstdCode = (TextInputLayout) findViewById(R.id.StdCode);
        parentEmail = (TextInputLayout) findViewById(R.id.parentEmail);
        parentPhoneNo = (TextInputLayout) findViewById(R.id.parentPhoneNo);
        parentPassword = (TextInputLayout) findViewById(R.id.parentPassword);
        parentConfirmPassword = (TextInputLayout) findViewById(R.id.parentConfirmPassword);
    }

    public void callVerifyScreen (View view){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        if (!validateName() | !validateSchoolCode() | !validateEmail() | !validatePhoneNumber() | !validatePassword()) {
            progressDialog.dismiss();
            return;
        }
        else {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("parent");
            String name = parentFullName.getEditText().getText().toString().trim();
            String stdCode = parentstdCode.getEditText().getText().toString().trim();
            String email = parentEmail.getEditText().getText().toString().trim();
            String num = parentPhoneNo.getEditText().getText().toString().trim();
            String phoneNo = "+975" + num;
            System.out.println("Number" + phoneNo);
            String password = parentPassword.getEditText().getText().toString().trim();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task)-> {
                if (!task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(ParentRegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ParentRegisterActivity.this, "Your Account has been successfully created", Toast.LENGTH_SHORT).show();
                    ParentHelperClass userHelperClass = new ParentHelperClass(name, stdCode, email, phoneNo, password);
                    progressDialog.dismiss();
                    reference.child(stdCode).setValue(userHelperClass);
                    parentFullName.getEditText().setText("");
                    parentstdCode.getEditText().setText("");
                    parentEmail.getEditText().setText("");
                    parentPhoneNo.getEditText().setText("");
                    parentPassword.getEditText().setText("");
                    parentConfirmPassword.getEditText().setText("");
                }
            });
        }
    }
    private boolean validateName(){
        String val = parentFullName.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            parentFullName.setError("Full Name is Required!");
            parentFullName.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validateSchoolCode(){
        String val = parentstdCode.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            parentstdCode.setError("Student Code is Required!");
            parentstdCode.requestFocus();
            return false;
        }
        else if(val.length() != 10) {
            parentstdCode.setError("School Code  should be exactly 10");
            parentstdCode.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validateEmail(){
        String val = parentEmail.getEditText().getText().toString().trim();
        final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+",Pattern.CASE_INSENSITIVE);
        final Pattern RUB_EMAIL_PATTERN = Pattern.compile("^[0-9]+\\.gcit@rub\\.edu\\.bt$",Pattern.CASE_INSENSITIVE);
        if(val.isEmpty()){
            parentEmail.setError("Email is Required!");
            parentEmail.requestFocus();
            return false;
        }
        else if(!RUB_EMAIL_PATTERN.matcher(val).matches()){
            parentEmail.setError("Please enter valid Email Address");
            parentEmail.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validatePhoneNumber(){
        String val = parentPhoneNo.getEditText().getText().toString().trim();
        //final Pattern TPHONE_NUMBER = Pattern.compile("[7]{2}[0-9]{6}",Pattern.CASE_INSENSITIVE);
        final Pattern BPHONE_NUMBER = Pattern.compile("[1][7][0-9]{6}",Pattern.CASE_INSENSITIVE);
        if(val.isEmpty()){
            parentPhoneNo.setError("Phone Number is Required!");
            parentPhoneNo.requestFocus();
            return false;
        }
        else if(!BPHONE_NUMBER.matcher(val).matches()){
            parentPhoneNo.setError("Invalid Phone Number");
            parentPhoneNo.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validatePassword(){
        String val = parentPassword.getEditText().getText().toString().trim();
        String val1 = parentConfirmPassword.getEditText().getText().toString().trim();
        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[@#$%^&+=])" +     // at least 1 special character
                        "(?=\\S+$)" +            // no white spaces
                        ".{4,}" +                // at least 4 characters
                        "$");
        if(val.isEmpty()){
            parentPassword.setError("Password is Empty");
            parentPassword.requestFocus();
            return false;
        }
        else if(val1.isEmpty()){
            parentConfirmPassword.setError("Confirm Password is Empty");
            parentConfirmPassword.requestFocus();
            return false;
        }
        else if(val.length() < 8) {
            parentPassword.setError("Password is too short, it should be at least 8");
            parentPassword.requestFocus();
            return false;
        }
//        else if(!PASSWORD_PATTERN.matcher(val).matches()){
//            parentPassword.setError("Password is too weak");
//            parentPassword.requestFocus();
//            return false;
//        }
        else if(!val.equals(val1)){
            parentConfirmPassword.setError("Confirm Password is didn't match");
            parentConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
    //Back button
    public void ParentRegisterToHome(View view) {
        Intent intent = new Intent(getApplicationContext(),AccountChooseActivity.class);
        intent.putExtra("schoolCode",s2);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.dismiss();
    }
}