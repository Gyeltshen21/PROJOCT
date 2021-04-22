package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText editTextSchoolCode, editTextEmail, editTextPhone, editTextPassword, editTextRetypePassword;
    Button register;
    TextView login;
    ProgressBar progressBar;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextSchoolCode = (EditText) findViewById(R.id.editTextSchoolCode);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        editTextRetypePassword = (EditText) findViewById(R.id.editTextTextRetypePassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        login = (TextView) findViewById(R.id.account);
        register = (Button) findViewById(R.id.Register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                //Get all the values from the text fields
                String SchoolCode = editTextSchoolCode.getText().toString();
                String email = editTextEmail.getText().toString();
                String Phone = editTextPhone.getText().toString();
                String password = editTextPassword.getText().toString();
                String RetypePassword = editTextRetypePassword.getText().toString();
                if(SchoolCode.isEmpty()){
                    editTextSchoolCode.setError("School Code Required");
                    editTextSchoolCode.requestFocus();
                }
                if(email.isEmpty()){
                    editTextEmail.setError("Email Required");
                    editTextEmail.requestFocus();
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Your Email is invalid");
                    editTextEmail.requestFocus();
                }
                if(Phone.isEmpty()){
                    editTextPhone.setError("Phone Number Required");
                    editTextPhone.requestFocus();
                }
                if(password.isEmpty()){
                    editTextPassword.setError("Password Required");
                    editTextPassword.requestFocus();
                }
                if(password.length() < 6){
                    Toast.makeText(MainActivity.this,"Password characters should be atleast six",Toast.LENGTH_SHORT).show();
                    return;
                }
                if((SchoolCode.isEmpty() && email.isEmpty() && Phone.isEmpty() && password.isEmpty() && RetypePassword.isEmpty())){
                    Toast.makeText(MainActivity.this, "The Fields are Empty",Toast.LENGTH_SHORT).show();
                }
                if((email.isEmpty() && password.isEmpty())){
                    Toast.makeText(MainActivity.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }
//                else if(RetypePassword.isEmpty()){
//                    editTextRetypePassword.setError("Please confirm your password");
//                    editTextRetypePassword.requestFocus();
//                }
//                else if(!(password.isEmpty() && RetypePassword.isEmpty())){
//                    if(password.equals(RetypePassword)){
//
//                    }
//                    else{
//                        editTextRetypePassword.setError("Confirm password didn't match");
//                        editTextRetypePassword.requestFocus();
//                    }
//                }
                if(!(email.isEmpty() && password.isEmpty())) {
                    progressBar.setVisibility(View.VISIBLE);
                    UserHelperClass userHelperClass = new UserHelperClass(SchoolCode, email, Phone, password);
                    reference.child(SchoolCode).setValue(userHelperClass);
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Not Registered, Please try again", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this, "Account Registered Successfully, Please check your Email for Verification", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        editTextSchoolCode.setText("");
                                        editTextEmail.setText("");
                                        editTextPhone.setText("");
                                        editTextPassword.setText("");
                                        editTextRetypePassword.setText("");
                                    }
                                });
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(MainActivity.this,"Not Registered, Please try again",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}