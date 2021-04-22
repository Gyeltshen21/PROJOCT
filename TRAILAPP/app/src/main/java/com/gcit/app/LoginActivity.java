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
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView register;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editTextLogin_Email);
        editTextPassword = (EditText) findViewById(R.id.editTextTextLogin_Password);
        register = (TextView) findViewById(R.id.Register);
        buttonLogin = (Button) findViewById(R.id.Login_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get all the values from the text field
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(email.isEmpty()){
                    editTextEmail.setError("Email Required");
                    editTextEmail.requestFocus();
                }
                else if(password.isEmpty()){
                    editTextPassword.setError("Password Required");
                    editTextPassword.requestFocus();
                }
                else if((email.isEmpty() && password.isEmpty())){
                    Toast.makeText(LoginActivity.this, "Please Fill up Details",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && password.isEmpty())){
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Error, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent1 = new Intent(LoginActivity.this,HomeActivity.class);
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent1);
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Login Error, Email Verification required",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Something went Wrong, Oops! Try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}