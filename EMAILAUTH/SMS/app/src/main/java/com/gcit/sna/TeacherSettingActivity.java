package com.gcit.sna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class TeacherSettingActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private EditText teacherEditTextFullName, teacherEditTextEmail, teacherEditTextPhoneNo;
    DatabaseReference databaseReference;
    String s1, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_setting);
        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        s1 = employeeID;
        databaseReference = FirebaseDatabase.getInstance().getReference("employee");
        teacherEditTextFullName = (EditText) findViewById(R.id.TeacherProfileName);
        teacherEditTextEmail = (EditText) findViewById(R.id.TeacherProfileEmail);
        teacherEditTextPhoneNo = (EditText) findViewById(R.id.TeacherProfilePhoneNo);

        //Set profile existing details
        Query checkUser = databaseReference.orderByChild("employeeID").equalTo(s1);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nameDB = snapshot.child(s1).child("name").getValue(String.class);
                    String emailDB = snapshot.child(s1).child("email").getValue(String.class);
                    String phoneDB = snapshot.child(s1).child("phone").getValue(String.class);
                    String passwordDB = snapshot.child(s1).child("password").getValue(String.class);
                    password = passwordDB;
                    teacherEditTextFullName.setText(nameDB);
                    teacherEditTextEmail.setText(emailDB);
                    teacherEditTextPhoneNo.setText(phoneDB);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Update button
    public void TeacherProfileUpdate(View view) {
        if(!validateName() | !validateEmail() | !validatePhoneNumber()){
            return;
        }
        else{
            String name = teacherEditTextFullName.getText().toString().trim();
            String email = teacherEditTextEmail.getText().toString().trim();
            String number = teacherEditTextPhoneNo.getText().toString().trim();
            Intent intent = new Intent(TeacherSettingActivity.this, SettingUpdateActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("phone", number);
            intent.putExtra("name", name);
            intent.putExtra("password", password);
            intent.putExtra("schoolCode", s1);
            intent.putExtra("whatToDo", "TeacherSettingUpdate");
            startActivity(intent);
            teacherEditTextFullName.setText("");
            teacherEditTextEmail.setText("");
            teacherEditTextPhoneNo.setText("");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();

        }
    }

    private boolean validateName(){
        String val = teacherEditTextFullName.getText().toString().trim();
        if(val.isEmpty()){
            teacherEditTextFullName.setError("Full Name is Required!");
            teacherEditTextFullName.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validateEmail(){
        String val = teacherEditTextEmail.getText().toString().trim();
        final Pattern RUB_EMAIL_PATTERN = Pattern.compile("^[0-9]+\\.gcit@rub\\.edu\\.bt$",Pattern.CASE_INSENSITIVE);
        if(val.isEmpty()){
            teacherEditTextEmail.setError("Email is Required!");
            teacherEditTextEmail.requestFocus();
            return false;
        }
        else if(!RUB_EMAIL_PATTERN.matcher(val).matches()){
            teacherEditTextEmail.setError("Please enter valid Email Address");
            teacherEditTextEmail.requestFocus();
            return false;
        }
        return true;
    }
    private boolean validatePhoneNumber(){
        String val = teacherEditTextPhoneNo.getText().toString().trim();
        //final Pattern TPHONE_NUMBER = Pattern.compile("[7]{2}[0-9]{6}",Pattern.CASE_INSENSITIVE);
        final Pattern BPHONE_NUMBER = Pattern.compile("[+][9][7][5][1][7][0-9]{6}",Pattern.CASE_INSENSITIVE);
        if(val.isEmpty()){
            teacherEditTextPhoneNo.setError("Phone Number is Required!");
            teacherEditTextPhoneNo.requestFocus();
            return false;
        }
        else if(!BPHONE_NUMBER.matcher(val).matches()){
            teacherEditTextPhoneNo.setError("Invalid Phone Number");
            teacherEditTextPhoneNo.requestFocus();
            return false;
        }
        return true;
    }

    public void BackToTeacherHome(View view) {
        Intent intent = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        intent.putExtra("employeeID",s1);
        startActivity(intent);
    }

    public void teacherChangePassword(View view) {
        Intent password = new Intent(getApplicationContext(),TeacherChangePasswordActivity.class);
        password.putExtra("employeeID",s1);
        startActivity(password);
    }
}