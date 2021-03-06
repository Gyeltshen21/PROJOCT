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

public class TeacherRegisterActivity extends AppCompatActivity {

    TextInputLayout teacherFullName, teacherEmployeeID, teacherEmail, teacherPhoneNo, teacherPassword, teacherConfirmPassword;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);
        firebaseAuth = FirebaseAuth.getInstance();

        String sCode = getIntent().getStringExtra("schoolCode");
        s2 = sCode;

        teacherFullName = (TextInputLayout) findViewById(R.id.EmlpoyeeName);
        teacherEmployeeID = (TextInputLayout) findViewById(R.id.EmlpoyeeID);
        teacherEmail = (TextInputLayout) findViewById(R.id.TeacherEmail);
        teacherPhoneNo = (TextInputLayout) findViewById(R.id.TeacherPhoneNo);
        teacherPassword = (TextInputLayout) findViewById(R.id.TeacherPassword);
        teacherConfirmPassword = (TextInputLayout) findViewById(R.id.TeacherConfirmPassword);
    }

    public void callVerifyScreen (View view){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        if (!validateName() | !validateSchoolCode() | !validateEmail() | !validatePhoneNumber() | !validatePassword()) {
            progressDialog.dismiss();
            return;
        }
        else {
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("employee");
            String name = teacherFullName.getEditText().getText().toString().trim();
            String employeeid = teacherEmployeeID.getEditText().getText().toString().trim();
            String email = teacherEmail.getEditText().getText().toString().trim();
            String num = teacherPhoneNo.getEditText().getText().toString().trim();
            String phoneNo = "+975" + num;
            String password = teacherPassword.getEditText().getText().toString().trim();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((task)-> {
                if (!task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(TeacherRegisterActivity.this, "Account not Registered, Please check your Details", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(TeacherRegisterActivity.this, "Account has been Registered Successfully", Toast.LENGTH_SHORT).show();
                    TeacherUserHelperClass userHelperClass = new TeacherUserHelperClass(name, employeeid, email, phoneNo, password);
                    reference.child(employeeid).setValue(userHelperClass);
                    teacherFullName.getEditText().setText("");
                    teacherEmployeeID.getEditText().setText("");
                    teacherEmail.getEditText().setText("");
                    teacherPhoneNo.getEditText().setText("");
                    teacherPassword.getEditText().setText("");
                    teacherConfirmPassword.getEditText().setText("");
                }
            });
        }
    }
    private boolean validateName(){
        String val = teacherFullName.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            teacherFullName.setError("Full Name is Required!");
            teacherFullName.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validateSchoolCode(){
        String val = teacherEmployeeID.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            teacherEmployeeID.setError("Employee ID is Required!");
            teacherEmployeeID.requestFocus();
            return false;
        }
        else if(val.length() != 11) {
            teacherEmployeeID.setError("Employee ID should be exactly 11");
            teacherEmployeeID.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validateEmail(){
        String val = teacherEmail.getEditText().getText().toString().trim();
        final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+",Pattern.CASE_INSENSITIVE);
        final Pattern RUB_EMAIL_PATTERN = Pattern.compile("^[0-9]+\\.gcit@rub\\.edu\\.bt$",Pattern.CASE_INSENSITIVE);
        if(val.isEmpty()){
            teacherEmail.setError("Email is Required!");
            teacherEmail.requestFocus();
            return false;
        }
        else if(!RUB_EMAIL_PATTERN.matcher(val).matches()){
            teacherEmail.setError("Please enter valid Email Address");
            teacherEmail.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validatePhoneNumber(){
        String val = teacherPhoneNo.getEditText().getText().toString().trim();
        //final Pattern TPHONE_NUMBER = Pattern.compile("[7]{2}[0-9]{6}",Pattern.CASE_INSENSITIVE);
        final Pattern BPHONE_NUMBER = Pattern.compile("[1][7][0-9]{6}",Pattern.CASE_INSENSITIVE);
        if(val.isEmpty()){
            teacherPhoneNo.setError("Phone Number is Required!");
            teacherPhoneNo.requestFocus();
            return false;
        }
        else if(!BPHONE_NUMBER.matcher(val).matches()){
            teacherPhoneNo.setError("Invalid Phone Number");
            teacherPhoneNo.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validatePassword(){
        String val = teacherPassword.getEditText().getText().toString().trim();
        String val1 = teacherConfirmPassword.getEditText().getText().toString().trim();
        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[@#$%^&+=])" +     // at least 1 special character
                        "(?=\\S+$)" +            // no white spaces
                        ".{4,}" +                // at least 4 characters
                        "$");
        if(val.isEmpty()){
            teacherPassword.setError("Password is Empty");
            teacherPassword.requestFocus();
            return false;
        }
        else if(val1.isEmpty()){
            teacherConfirmPassword.setError("Confirm Password is Empty");
            teacherConfirmPassword.requestFocus();
            return false;
        }
        else if(val.length() < 8) {
            teacherPassword.setError("Password is too short, it should be at least 8");
            teacherPassword.requestFocus();
            return false;
        }
//        else if(!PASSWORD_PATTERN.matcher(val).matches()){
//            teacherPassword.setError("Password is too weak");
//            teacherPassword.requestFocus();
//            return false;
//        }
        else if(!val.equals(val1)){
            teacherConfirmPassword.setError("Confirm Password is didn't match");
            teacherConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
    //Back button
    public void TeacherRegisterToHome(View view) {
        Intent intent = new Intent(getApplicationContext(),AccountChooseActivity.class);
        intent.putExtra("schoolCode",s2);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("Please wait");
        progressDialog.dismiss();
    }
}