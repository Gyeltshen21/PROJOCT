package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ParentVerifyEmailActivity extends AppCompatActivity {

    Button email_verify_btn;
    TextView verify_header;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String sCode;
    String name, phoneNo, stdCode, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_verify_email);

        firebaseAuth = FirebaseAuth.getInstance();
        String name = getIntent().getStringExtra("parentFullName");
        String stdCode = getIntent().getStringExtra("stdCode");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        String phoneNo = getIntent().getStringExtra("phoneNo");

        Intent intent = getIntent();
        String Code = intent.getStringExtra("schoolCode");
        sCode = Code;
        email_verify_btn = (Button) findViewById(R.id.email_next_btn);
        verify_header = (TextView) findViewById(R.id.verify_header);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("parent");
        email_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(ParentVerifyEmailActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(ParentVerifyEmailActivity.this, "Login Error, Please Try Again",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                ParentHelperClass userHelperClass = new ParentHelperClass(name, stdCode, email, phoneNo, password);
                                reference.child(stdCode).setValue(userHelperClass);
                                Intent intent1 = new Intent(ParentVerifyEmailActivity.this,LoginActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent1);
                                finish();
                            }
                            else{
                                Toast.makeText(ParentVerifyEmailActivity.this, "Login Error, Email Verification required",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }
    public void reSendLink(View view) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(!firebaseUser.isEmailVerified()){
            verify_header.setVisibility(View.VISIBLE);
            email_verify_btn.setVisibility(View.VISIBLE);
            email_verify_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ParentVerifyEmailActivity.this, "Verification link has been sent to your Email, Please check your Email", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ParentVerifyEmailActivity.this, "onFailure link not sent, Please try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public void parentVerificationToparentRegister(View view) {
        Intent intent = new Intent(getApplicationContext(),ParentRegisterActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }
}