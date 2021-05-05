package com.gcit.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gcit.sms.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //if opt sent is failed, will used to resent code
    private PhoneAuthProvider.ForceResendingToken forceResending;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId; //will hold OTPVerification code

    private static final String TAG = "MAIN_TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.phoneLl.setVisibility(View.VISIBLE); //show phone layout
        binding.codeLl.setVisibility(View.GONE);//hide code layout, when OTP sent then hide phone layout
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG,"onCodeSent: " +verificationId);
                mVerificationId = verificationId;
                forceResending = forceResendingToken;
                progressDialog.dismiss();

                binding.phoneLl.setVisibility(View.GONE);
                binding.codeLl.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(),"Verification code sen",Toast.LENGTH_SHORT).show();
                binding.codeSentDescription.setText("Please enter your verification code we sent \n " +binding.phoneEt.getText().toString().trim());
            }
        };
        binding.phoneContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.phoneEt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(getApplicationContext(),"Please enter your phone number",Toast.LENGTH_SHORT).show();
                }
                else{
                    startPhoneNumberVerification(phone);
                }
            }
        });
        binding.resentCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.phoneEt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(getApplicationContext(),"Please enter your phone number",Toast.LENGTH_SHORT).show();
                }
                else{
                    resendVerification(phone, forceResending);
                }
            }
        });
        binding.codeSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= binding.codeEt.getText().toString().trim();
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
        progressDialog.setMessage("Logged In");
        progressDialog.show();
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                Toast.makeText(getApplicationContext(),"Logged In as" +phone, Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
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