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

public class PhoneAuthActivity extends AppCompatActivity {
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    //if opt sent is failed, will used to resent code
    private PhoneAuthProvider.ForceResendingToken forceResending;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId; //will hold OTPVerification code
    private LinearLayout phoneLl, codeLl;
    private TextView codeSentDescription, resentCodeTv;
    private EditText phoneEt, codeEt;
    private Button phoneContinueBtn, codeSubmitBtn;
    private static final String TAG = "MAIN_TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    String phone, schoolCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        String s1 = getIntent().getStringExtra("schoolCode");
        schoolCode = s1;

        phoneLl = (LinearLayout) findViewById(R.id.phoneLl);
        codeLl = (LinearLayout) findViewById(R.id.codeLl);
        phoneEt = (EditText) findViewById(R.id.phoneEt);
        codeEt = (EditText) findViewById(R.id.codeEt);
        codeSentDescription = (TextView) findViewById(R.id.codeSentDescription);
        resentCodeTv = (TextView) findViewById(R.id.resentCodeTv);
        phoneContinueBtn = (Button) findViewById(R.id.phoneContinueBtn);
        codeSubmitBtn = (Button) findViewById(R.id.codeSubmitBtn);
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

                Toast.makeText(PhoneAuthActivity.this,"Verification code sent",Toast.LENGTH_SHORT).show();
                codeSentDescription.setText("Please enter your verification code we sent \n " +phoneEt.getText().toString().trim());
            }
        };
        phoneContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneEt.getText().toString().trim();
                String phone = "+975" + number;
                String phone1 = getIntent().getStringExtra("phone");
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(PhoneAuthActivity.this,"Please enter your phone number",Toast.LENGTH_SHORT).show();
                }
                if(!phone.equals(phone1)){
                    progressDialog.dismiss();
                    phoneEt.setError("No such phone number" + phone + " in employeeID/SchoolCode/StudentCode :" +schoolCode);
                    phoneEt.requestFocus();
                }
                else{
                    startPhoneNumberVerification(phone);
                }
            }
        });

        //resendCodeTv click: (if code didn't receive) resend verification code / OTP
        resentCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneEt.getText().toString().trim();
                String phone = "+975" +number;
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(getApplicationContext(),"Please enter your phone number",Toast.LENGTH_SHORT).show();
                }
                else{
                    resendVerification(phone, forceResending);
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
                String phoneNo = firebaseAuth.getCurrentUser().getPhoneNumber();
                //Password Update
                if(whatToDo.equals("AdminUpdate")){
                        updateOldUserData();
                }
                else if(whatToDo.equals("EmployeeUpdate")){
                    updateOldEmployeeUserData();
                }
                else if(whatToDo.equals("ParentUpdate")){
                        updateOldParentUserData();
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

    //Update forgot password of admin
    private void updateOldUserData() {
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), SetNewPasswordActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("schoolCode",schoolCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    //Update forgot password of employee
    private void updateOldEmployeeUserData() {
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), TeacherSetNewPasswordActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("schoolCode",schoolCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    //Update forgot password of parent
    private void updateOldParentUserData() {
        progressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), ParentSetNewPasswordActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("schoolCode",schoolCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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

    public void BackToForgotPasswordPage(View view) {
        Intent intent = new Intent(PhoneAuthActivity.this, ForgotPasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}