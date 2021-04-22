package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class PhoneNoActivity extends AppCompatActivity {

    ScrollView scrollView;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_no);

        phoneNumber = (TextInputLayout) findViewById(R.id.phoneNumber);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.country_code_picker);
        countryCodePicker.resetToDefaultCountry();
    }

    public void callVerifyOTPScreen(View view) {
        if(!validatePhoneNumber()){
            return;
        }
        String _schoolCode = getIntent().getStringExtra("SCHOOLCODE");
        String _email = getIntent().getStringExtra("EMAIL");
        String _password = getIntent().getStringExtra("PASSWORD");
        String _getUserEnterPhoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String _phoneNo = "+" + countryCodePicker.getSelectedCountryCode()+ _getUserEnterPhoneNumber;

        Intent phoneNoIntent = new Intent(getApplicationContext(),OTPActivity.class);
        phoneNoIntent.putExtra("sCode",_schoolCode);
        phoneNoIntent.putExtra("email",_email);
        phoneNoIntent.putExtra("password",_password);
        phoneNoIntent.putExtra("pNo",_phoneNo);
        startActivity(phoneNoIntent);
    }
    private boolean validatePhoneNumber(){
        String val = phoneNumber.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            phoneNumber.setError("Phone Number is Required!");
            phoneNumber.requestFocus();
            return false;
        }
        return true;
    }
}