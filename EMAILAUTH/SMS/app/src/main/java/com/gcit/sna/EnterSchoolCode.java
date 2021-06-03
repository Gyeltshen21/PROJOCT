package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnterSchoolCode extends AppCompatActivity {

    EditText schoolCode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_school_code);
        schoolCode = findViewById(R.id.code);
        reference = FirebaseDatabase.getInstance().getReference("SCHOOLCODE");

    }

    public void addSchoolCode(View view) {
        if(!validateCode()){
            return;
        }
        else{
            String code = schoolCode.getText().toString().trim();
            CodeHelper codeHelper = new CodeHelper(code);
            reference.child(code).setValue(codeHelper);
            Toast.makeText(getApplicationContext(),"Added successfully",Toast.LENGTH_SHORT).show();
            schoolCode.setText("");
        }
    }

    private boolean validateCode() {
        String val = schoolCode.getText().toString().trim();
        if(val.isEmpty()){
            schoolCode.setError("Field cannot be empty!");
            schoolCode.requestFocus();
            return false;
        }
        else if(val.length() != 3){
            schoolCode.setError("School Code should be exactly 3");
            schoolCode.requestFocus();
            return false;
        }
        return true;
    }
}