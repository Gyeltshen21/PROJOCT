package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class TeacherSetNewPasswordActivity extends AppCompatActivity {

    TextInputLayout editTextPassword, editTextConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_set_new_password);

        editTextPassword = (TextInputLayout) findViewById(R.id.password);
        editTextConfirmPassword = (TextInputLayout) findViewById(R.id.confirmpassword);
    }

    public void updateNewPassword(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Password");
        progressDialog.setMessage("Updating new Password...");
        progressDialog.show();
        if(!validatePassword()){
            //progressDialog.dismiss();
            return;
        }
        else{
            String schoolCode = getIntent().getStringExtra("schoolCode");
            String _newPassword = editTextPassword.getEditText().getText().toString().trim();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("employee");
            reference.child(schoolCode).child("password").setValue(_newPassword);
            Intent intent = new Intent(TeacherSetNewPasswordActivity.this, UpdatedForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        }
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
}