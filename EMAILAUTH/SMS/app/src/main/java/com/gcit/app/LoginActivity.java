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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout editTextSchoolCode, editTextPassword;
    FirebaseAuth firebaseAuth;
    final loadingDailogue dailogue = new loadingDailogue(LoginActivity.this);
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
        dailogue.startLoadingDialogue();
        if (!validateSchoolCode() | !validatePassword()) {
            return;
        }
        //Admin Condition
        else {
            int countNo = editTextSchoolCode.getEditText().length();
            System.out.println("COUNT" +countNo);
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
                                String emailDB = snapshot.child(schoolCode).child("email").getValue(String.class);
                                String phoneDB = snapshot.child(schoolCode).child("phone").getValue(String.class);
                                String schoolCodeDB = snapshot.child(schoolCode).child("schoolCode").getValue(String.class);
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("email", emailDB);
                                intent.putExtra("phone", phoneDB);
                                intent.putExtra("schoolCode", schoolCodeDB);
                                startActivity(intent);
                            } else {
                                editTextPassword.setError("Wrong password");
                                editTextPassword.requestFocus();
                            }
                        }
                        else{
                            dailogue.dismiss();
                            Toast.makeText(getApplicationContext(),"No such Account",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            //Teacher login conditions
            else if(countNo == 11){
                String schoolCode = editTextSchoolCode.getEditText().getText().toString().trim();
                String password = editTextPassword.getEditText().getText().toString().trim();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("employee");
                Query checkUser = databaseReference.orderByChild("employeeID").equalTo(schoolCode);
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
                                String emailDB = snapshot.child(schoolCode).child("email").getValue(String.class);
                                String phoneDB = snapshot.child(schoolCode).child("phone").getValue(String.class);
                                String schoolCodeDB = snapshot.child(schoolCode).child("schoolCode").getValue(String.class);
                                Intent intent = new Intent(getApplicationContext(), TeacherHomeActivity.class);
                                startActivity(intent);
                            } else {
                                editTextPassword.setError("Wrong password");
                                editTextPassword.requestFocus();
                            }
                        }
                        else{
                            dailogue.dismiss();
                            Toast.makeText(getApplicationContext(),"No such Account",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
//            else if(schoolCode.equals("15")){
//                String schoolCode = editTextSchoolCode.getEditText().getText().toString().trim();
//                String password = editTextPassword.getEditText().getText().toString().trim();
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
//                Query checkUser = databaseReference.orderByChild("schoolCode").equalTo(schoolCode);
//                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            editTextSchoolCode.setError(null);
//                            editTextSchoolCode.setEnabled(false);
//                            String passwordDB = snapshot.child(schoolCode).child("password").getValue(String.class);
//                            if (passwordDB.equals(password)) {
//                                editTextPassword.setError(null);
//                                editTextPassword.setEnabled(false);
//                                String emailDB = snapshot.child(schoolCode).child("email").getValue(String.class);
//                                String phoneDB = snapshot.child(schoolCode).child("phone").getValue(String.class);
//                                String schoolCodeDB = snapshot.child(schoolCode).child("schoolCode").getValue(String.class);
//                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                                intent.putExtra("email", emailDB);
//                                intent.putExtra("password", passwordDB);
//                                intent.putExtra("phone", phoneDB);
//                                intent.putExtra("schoolCode", schoolCodeDB);
//                                startActivity(intent);
//                            } else {
//                                editTextPassword.setError("Wrong password");
//                                editTextPassword.requestFocus();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
            else{
                dailogue.dismiss();
                Toast.makeText(getApplicationContext(),"No such Account",Toast.LENGTH_SHORT).show();
            }
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
        dailogue.startLoadingDialogue();
        Intent loginIntent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(loginIntent);
    }
}