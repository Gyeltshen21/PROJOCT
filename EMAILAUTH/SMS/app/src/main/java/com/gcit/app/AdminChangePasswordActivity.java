package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class AdminChangePasswordActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private TextInputLayout oldPassword, newPassword, confirmPassword;
    private TextView changePasswordMessage;
    private Button btn;
    private String s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_password);

        oldPassword = (TextInputLayout)findViewById(R.id.oldpassword);
        newPassword = (TextInputLayout) findViewById(R.id.password);
        confirmPassword = (TextInputLayout) findViewById(R.id.confirmpassword);
        changePasswordMessage = (TextView)findViewById(R.id.updatedMessage);
        btn = (Button) findViewById(R.id.btn);

        String schoolCode = getIntent().getStringExtra("schoolCode");
        s1 = schoolCode;
    }

    public void changeNewPassword(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String oldPasswordFromUser = oldPassword.getEditText().getText().toString().trim();
        String newPasswordFormUser = newPassword.getEditText().getText().toString().trim();
        reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("schoolCode").equalTo(s1);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String oldPasswordDB = snapshot.child(s1).child("password").getValue(String.class);
                    if(oldPasswordDB.equals(oldPasswordFromUser)){
                        oldPassword.setError(null);
                        oldPassword.setEnabled(false);
                        if(!validatePassword()){
                            progressDialog.dismiss();
                            return;
                        }
                        else{
                            progressDialog.dismiss();
                            reference.child(s1).child("password").setValue(newPasswordFormUser);
                            oldPassword.setVisibility(View.GONE);
                            newPassword.setVisibility(View.GONE);
                            confirmPassword.setVisibility(View.GONE);
                            btn.setVisibility(View.GONE);
                            changePasswordMessage.setVisibility(View.VISIBLE);
                            changePasswordMessage.setText("Has been changed successfully");
                        }
                    }
                    else{
                        progressDialog.dismiss();
                        oldPassword.setError("Wrong password");
                        oldPassword.requestFocus();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"No such schoolCode",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    private boolean validatePassword(){
        String val = newPassword.getEditText().getText().toString().trim();
        String val1 = confirmPassword.getEditText().getText().toString().trim();
        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[@#$%^&+=])" +     // at least 1 special character
                        "(?=\\S+$)" +            // no white spaces
                        ".{4,}" +                // at least 4 characters
                        "$");
        if(val.isEmpty()){
            newPassword.setError("Password is Empty");
            newPassword.requestFocus();
            return false;
        }
        else if(val1.isEmpty()){
            confirmPassword.setError("Confirm Password is Empty");
            confirmPassword.requestFocus();
            return false;
        }
        else if(!val.isEmpty() && val1.isEmpty()){
            confirmPassword.setError("Confirm Password is Empty");
            confirmPassword.requestFocus();
            return false;
        }
        else if(val.length() < 8) {
            newPassword.setError("Password is too short, it should be at least 8");
            newPassword.requestFocus();
            return false;
        }
//        else if(!PASSWORD_PATTERN.matcher(val).matches()){
//            newPassword.setError("Password is too weak");
//            newPassword.requestFocus();
//            return false;
//        }
        else if(!val.equals(val1)){
            confirmPassword.setError("Confirm Password is didn't match");
            confirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    public void BackToAdminHome(View view) {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("schoolCode",s1);
        startActivity(intent);
    }
}