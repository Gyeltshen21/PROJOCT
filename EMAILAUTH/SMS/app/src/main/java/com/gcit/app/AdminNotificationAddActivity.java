package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminNotificationAddActivity extends AppCompatActivity {

    private FirebaseDatabase rootNode;
    private DatabaseReference databaseReference;
    private EditText AdminNotificationSenderName, AdminNotificationDate, AdminNotificationTime, AdminNotificationMessage;

    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification_add);

        Intent intent = getIntent();
        String Code = intent.getStringExtra("schoolCode");
        sCode = Code;

        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("AdminNotification");

        AdminNotificationSenderName = (EditText) findViewById(R.id.AdminNotificationSenderName);
        AdminNotificationDate = (EditText) findViewById(R.id.AdminNotificationDate);
        AdminNotificationTime = (EditText) findViewById(R.id.AdminNotificationTime);
        AdminNotificationMessage = (EditText) findViewById(R.id.AdminNotificationMessage);
    }

    public void BackToAdminNotification(View view) {
        Intent intent = new Intent(getApplicationContext(),AdminNotificationActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }

    public void SendNotification(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        if(!validateSenderName() | !validateTime() | !validateDate() | !validateMessage()){
            progressDialog.dismiss();
            return;
        }
        else{
            String AdminNotificationSender_Name = AdminNotificationSenderName.getText().toString().trim();
            String AdminNotification_Date = AdminNotificationDate.getText().toString().trim();
            String AdminNotification_Time = AdminNotificationTime.getText().toString().trim();
            String AdminNotification_Message = AdminNotificationMessage.getText().toString().trim();

            AdminNotificationHelperClass adminNotificationHelperClass = new AdminNotificationHelperClass(AdminNotificationSender_Name, AdminNotification_Date, AdminNotification_Time,AdminNotification_Message);
            databaseReference.child(AdminNotification_Time).setValue(adminNotificationHelperClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Message has been sent successfully",Toast.LENGTH_SHORT).show();
                    AdminNotificationSenderName.setText("");
                    AdminNotificationDate.setText("");
                    AdminNotificationTime.setText("");
                    AdminNotificationMessage.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateMessage() {
        String val = AdminNotificationMessage.getText().toString().trim();
        if(val.isEmpty()){
            AdminNotificationMessage.setError("Field cannot be empty");
            AdminNotificationMessage.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateDate() {
        String val = AdminNotificationDate.getText().toString().trim();
        if(val.isEmpty()){
            AdminNotificationDate.setError("Field cannot be empty");
            AdminNotificationDate.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateTime() {
        String val = AdminNotificationTime.getText().toString().trim();
        if(val.isEmpty()){
            AdminNotificationTime.setError("Field cannot be empty");
            AdminNotificationTime.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateSenderName() {
        String val = AdminNotificationSenderName.getText().toString().trim();
        if(val.isEmpty()){
            AdminNotificationSenderName.setError("Field cannot be empty");
            AdminNotificationSenderName.requestFocus();
            return false;
        }
        return true;
    }

    public void SelectTime(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(AdminNotificationAddActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Initialize hour and minute
                int t2Hour = hourOfDay;
                int t2Minute = minute;

                //Store hour and minute in string
                String time = t2Hour + ":" + t2Minute;
                //Initialize 24 hours time format
                SimpleDateFormat f24format = new SimpleDateFormat(
                        "HH:mm"
                );
                try {
                    Date date = f24format.parse(time);
                    //Initialize 12 hours time format
                    SimpleDateFormat f12format = new SimpleDateFormat(
                            "hh:mm aa"
                    );
                    //Set selected time on text view
                    AdminNotificationTime.setText(f12format.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, 12, 0, false

        );
        //Set transparent background
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }

    public void ChooseDate(View view) {
        DialogFragment newFragment = new AdminNotificationDateFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void adminNotificationResultPicker(int year, int month, int dayOfMonth) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(dayOfMonth);
        String year_string = Integer.toString(year);

        String date_message = (day_string + "/" + month_string + "/" + year_string);
        AdminNotificationDate.setText(date_message);
    }

}