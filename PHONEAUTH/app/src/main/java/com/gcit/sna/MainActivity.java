package com.gcit.sna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText editTextSchoolCode, editTextPassword;
    Button buttonLogin;
    TextView register;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextSchoolCode = (EditText) findViewById(R.id.editTextLogin_SchoolCode);
        editTextPassword = (EditText) findViewById(R.id.editTextTextLogin_Password);
        register = (TextView) findViewById(R.id.Register);
        buttonLogin = (Button) findViewById(R.id.Login_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get all the values from the text field
                String schoolCode = editTextSchoolCode.getText().toString();
                String password = editTextPassword.getText().toString();

                if(schoolCode.isEmpty()){
                    editTextSchoolCode.setError("School Code Required");
                    editTextSchoolCode.requestFocus();
                }
                else if(password.isEmpty()){
                    editTextPassword.setError("Password Required");
                    editTextPassword.requestFocus();
                }
                else if((schoolCode.isEmpty() && password.isEmpty())){
                    Toast.makeText(MainActivity.this, "Please Fill up Details",Toast.LENGTH_SHORT).show();
                }
                else if(!(schoolCode.isEmpty() && password.isEmpty())){
                    progressBar.setVisibility(View.VISIBLE);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    Query checkUser = databaseReference.orderByChild("schoolCode").equalTo(schoolCode);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                progressBar.setVisibility(View.GONE);
                                editTextSchoolCode.setError(null);
                                editTextSchoolCode.setEnabled(false);
                                String passwordDB = snapshot.child(schoolCode).child("password").getValue(String.class);
                                if(passwordDB.equals(password)){
                                    progressBar.setVisibility(View.GONE);
                                    editTextPassword.setError(null);
                                    editTextPassword.setEnabled(false);
                                    String emailDB = snapshot.child(schoolCode).child("email").getValue(String.class);
                                    String phoneDB = snapshot.child(schoolCode).child("phone").getValue(String.class);
                                    String schoolCodeDB = snapshot.child(schoolCode).child("schoolCode").getValue(String.class);
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.putExtra("email",emailDB);
                                    intent.putExtra("password",passwordDB);
                                    intent.putExtra("phone",phoneDB);
                                    intent.putExtra("schoolCode",schoolCodeDB);
                                    startActivity(intent);
                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    editTextPassword.setError("Wrong password");
                                    editTextPassword.requestFocus();
                                }
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                editTextSchoolCode.setError("No such school code");
                                editTextSchoolCode.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this,"Account signup Failed",Toast.LENGTH_SHORT).show();
                        }
                    });

//                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(!task.isSuccessful()){
//                                Toast.makeText(LoginActivity.this, "Login Error, Please Try Again",Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//                                if(firebaseAuth.getCurrentUser().isEmailVerified()){
//                                    progressBar.setVisibility(View.GONE);
//                                    Intent intent1 = new Intent(LoginActivity.this,HomeActivity.class);
//                                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent1);
//                                }
//                                else{
//                                    Toast.makeText(LoginActivity.this, "Login Error, Email Verification required",Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    });
                }
                else{
                    Toast.makeText(MainActivity.this, "Something went Wrong, Oops! Try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}