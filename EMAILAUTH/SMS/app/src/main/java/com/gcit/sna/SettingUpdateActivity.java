package com.gcit.sna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SettingUpdateActivity extends AppCompatActivity {

    //if opt sent is failed, will used to resent code
    private PhoneAuthProvider.ForceResendingToken forceResending;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId; //will hold OTPVerification code
    private LinearLayout phoneLl, codeLl;
    private TextView codeSentDescription, resentCodeTv;
    private TextView phoneEt;
    private EditText codeEt;
    private Button phoneContinueBtn, codeSubmitBtn;
    private static final String TAG = "MAIN_TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    String phone, schoolCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_update);
        String s1 = getIntent().getStringExtra("schoolCode");
        String p = getIntent().getStringExtra("phone");
        schoolCode = s1;

        phoneLl = (LinearLayout) findViewById(R.id.phoneLl);
        codeLl = (LinearLayout) findViewById(R.id.codeLl);
        phoneEt = (TextView)findViewById(R.id.phoneEt);
        codeEt = (EditText) findViewById(R.id.codeEt);
        codeSentDescription = (TextView) findViewById(R.id.codeSentDescription);
        resentCodeTv = (TextView) findViewById(R.id.resentCodeTv);
        phoneContinueBtn = (Button) findViewById(R.id.phoneContinueBtn);
        codeSubmitBtn = (Button) findViewById(R.id.codeSubmitBtn);
        phoneEt.setText(p);
        phoneLl.setVisibility(View.VISIBLE); //show phone layout
        codeLl.setVisibility(View.GONE);//hide code layout, when OTP sent then hide phone layout
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            //This callback will be invoked in two situations:
            //1 - Instant verification. In some cases the phone number can instantly
            //verified without needing to send or enter the verification code
            //2 - Auto-retrieval. On some devices Google Play Services can automatically
            //detect the incoming verification SMS and perform verification without user-action
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification code is made,'
                // for instance if the phone number format is not invalid
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with verification ID.
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"onCodeSent: " +verificationId);
                mVerificationId = verificationId;
                forceResending = forceResendingToken;
                progressDialog.dismiss();

                phoneLl.setVisibility(View.GONE);
                codeLl.setVisibility(View.VISIBLE);

                Toast.makeText(SettingUpdateActivity.this,"Verification code sent",Toast.LENGTH_SHORT).show();
                codeSentDescription.setText("Please enter your verification code we sent \n " +phoneEt.getText().toString().trim());
            }
        };
        phoneContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneEt.getText().toString().trim();
                phone = number;
                if(TextUtils.isEmpty(number)){
                    Toast.makeText(SettingUpdateActivity.this,"Please enter your phone number",Toast.LENGTH_SHORT).show();
                }
                else{
                    startPhoneNumberVerification(number);
                }
            }
        });

        //resendCodeTv click: (if code didn't receive) resend verification code / OTP
        resentCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneEt.getText().toString().trim();
                phone = number;
                if(TextUtils.isEmpty(number)){
                    Toast.makeText(getApplicationContext(),"Please enter your phone number",Toast.LENGTH_SHORT).show();
                }
                else{
                    resendVerification(number, forceResending);
                }
            }
        });
        codeSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEt.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(getApplicationContext(),"Please enter verification code",Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyPhoneNumberWithCode(mVerificationId,code);
                }
            }
        });
    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        progressDialog.setMessage("Verifying Code...");
        progressDialog.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");
                String password = getIntent().getStringExtra("password");
                String whatToDo = getIntent().getStringExtra("whatToDo");
                //Password Update
                if(whatToDo.equals("AdminSettingUpdate")){
                    AdminSettingUpdate(name, email);
                }
                else if(whatToDo.equals("TeacherSettingUpdate")){
                    TeacherSettingUpdate(name, email);
                }
                else if(whatToDo.equals("ParentSettingUpdate")){
                    ParentSettingUpdate(name, email);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Admin Profile new change update
    private void AdminSettingUpdate(String name, String email) {
        //Set profile existing details
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("schoolCode").equalTo(schoolCode);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reference.child(schoolCode).child("name").setValue(name);
                    reference.child(schoolCode).child("email").setValue(email);
                    reference.child(schoolCode).child("phone").setValue(phone);
                    Toast.makeText(getApplicationContext(),"Profile has been updated successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingUpdateActivity.this, AdminProfileActivity.class);
                    intent.putExtra("schoolCode",schoolCode);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Teacher Profile new change update
    private void TeacherSettingUpdate(String name, String email) {
        //Set profile existing details
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("employee");
        Query checkUser = reference.orderByChild("employeeID").equalTo(schoolCode);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reference.child(schoolCode).child("name").setValue(name);
                    reference.child(schoolCode).child("email").setValue(email);
                    reference.child(schoolCode).child("phone").setValue(phone);
                    Toast.makeText(getApplicationContext(),"Profile has been updated successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingUpdateActivity.this, TeacherProfileActivity.class);
                    intent.putExtra("employeeID",schoolCode);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Parent Profile new change update
    private void ParentSettingUpdate(String name, String email) {
        //Set profile existing details
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("parent");
        Query checkUser = reference.orderByChild("stdCode").equalTo(schoolCode);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reference.child(schoolCode).child("name").setValue(name);
                    reference.child(schoolCode).child("email").setValue(email);
                    reference.child(schoolCode).child("phone").setValue(phone);
                    Toast.makeText(getApplicationContext(),"Profile has been updated successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingUpdateActivity.this, ParentProfileActivity.class);
                    intent.putExtra("stdCode",schoolCode);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendVerification(String phone, PhoneAuthProvider.ForceResendingToken token) {
        progressDialog.setMessage("Resending Code...");
        progressDialog.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60l, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startPhoneNumberVerification(String phone) {
        progressDialog.setMessage("Verifying Phone Number");
        progressDialog.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60l, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}