package com.gcit.sna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
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

public class LoginActivity extends AppCompatActivity {
    TextInputLayout editTextSchoolCode, editTextPassword;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextSchoolCode = (TextInputLayout) findViewById(R.id.editTextLogin_SchoolCode);
        editTextPassword = (TextInputLayout) findViewById(R.id.editTextTextLogin_Password);
    }

    public void callHomePage(View view) {
        if(!isConnected(this)){
            showCustomDialog();
        }
        else{
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            if (!validateSchoolCode() | !validatePassword()) {
                progressDialog.dismiss();
                return;
            }
            //Admin Condition
            else {
                int countNo = editTextSchoolCode.getEditText().length();
                System.out.println("DDDD"+countNo);
                if(countNo == 3){
                    String schoolCode = editTextSchoolCode.getEditText().getText().toString().trim();
                    String password = editTextPassword.getEditText().getText().toString().trim();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    Query checkUser = databaseReference.orderByChild("schoolCode").equalTo(schoolCode);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                editTextSchoolCode.setError(null);
                                editTextSchoolCode.setEnabled(false);
                                String passwordDB = snapshot.child(schoolCode).child("password").getValue(String.class);
                                if (passwordDB.equals(password)) {
                                    editTextPassword.setError(null);
                                    editTextPassword.setEnabled(false);
                                    String schoolCodeDB = snapshot.child(schoolCode).child("schoolCode").getValue(String.class);
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.putExtra("schoolCode", schoolCodeDB);
                                    startActivity(intent);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    editTextPassword.setError("Wrong password");
                                    editTextPassword.requestFocus();
                                }
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No such Account",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //Teacher login conditions
                else if(countNo == 11){
                    String employeeID = editTextSchoolCode.getEditText().getText().toString().trim();
                    String password = editTextPassword.getEditText().getText().toString().trim();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("employee");
                    Query checkUser = databaseReference.orderByChild("employeeID").equalTo(employeeID);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                editTextSchoolCode.setError(null);
                                editTextSchoolCode.setEnabled(false);
                                String passwordDB = snapshot.child(employeeID).child("password").getValue(String.class);
                                if (passwordDB.equals(password)) {
                                    editTextPassword.setError(null);
                                    editTextPassword.setEnabled(false);
                                    String employeeIDDB = snapshot.child(employeeID).child("employeeID").getValue(String.class);
                                    Intent intent = new Intent(getApplicationContext(), TeacherHomeActivity.class);
                                    intent.putExtra("employeeID", employeeIDDB);
                                    startActivity(intent);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    editTextPassword.setError("Wrong password");
                                    editTextPassword.requestFocus();
                                }
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No such Account",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //Parent login conditions
                else if(countNo == 10){
                    String stdCode = editTextSchoolCode.getEditText().getText().toString().trim();
                    String password = editTextPassword.getEditText().getText().toString().trim();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("parent");
                    Query checkUser = databaseReference.orderByChild("stdCode").equalTo(stdCode);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                editTextSchoolCode.setError(null);
                                editTextSchoolCode.setEnabled(false);
                                String passwordDB = snapshot.child(stdCode).child("password").getValue(String.class);
                                if (passwordDB.equals(password)) {
                                    editTextPassword.setError(null);
                                    editTextPassword.setEnabled(false);
                                    String stdCodeDB = snapshot.child(stdCode).child("stdCode").getValue(String.class);
                                    Intent intent = new Intent(getApplicationContext(), ParentHomeActivity.class);
                                    intent.putExtra("stdCode", stdCodeDB);
                                    startActivity(intent);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    editTextPassword.setError("Wrong password");
                                    editTextPassword.requestFocus();
                                }
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"No such Account",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"No such Account",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Please connect to internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //Check internet connection

    private boolean isConnected(LoginActivity loginActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean validateSchoolCode(){
        String val = editTextSchoolCode.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            editTextSchoolCode.setError("School Code/ EmployeeID/ Student Code is Required!");
            editTextSchoolCode.requestFocus();
            return false;
        }
        else if(val.length() > 14) {
            editTextSchoolCode.setError("School Code/ EmployeeID/ Student Code is too large, it should less than 14");
            editTextSchoolCode.requestFocus();
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
        else if(val.length() < 8) {
            editTextPassword.setError("Password is too short, it should be at least 8");
            editTextPassword.requestFocus();
            return false;
        }
        return true;
    }

    public void callRegisterPage(View view) {
        if(!isConnected(this)){
            showCustomDialog();
        }
        else{
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            Intent loginIntent = new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(loginIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.dismiss();
    }

    public void callForgotPasswordPage(View view) {
        if(!isConnected(this)){
            showCustomDialog();
        }
        else{
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            Intent loginIntent = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
            startActivity(loginIntent);
        }
    }
}