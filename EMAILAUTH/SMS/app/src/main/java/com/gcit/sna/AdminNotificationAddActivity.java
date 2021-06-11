package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AdminNotificationAddActivity extends AppCompatActivity {

    private EditText AdminNotificationSenderName, AdminNotificationMessage;
    private TextView AdminNotificationSpinner;
    private Spinner spinner;
    private Button notification;
    private Context mContext = AdminNotificationAddActivity.this;

    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification_add);
        AdminNotificationSenderName = (EditText) findViewById(R.id.AdminNotificationSenderName);
        AdminNotificationMessage = (EditText) findViewById(R.id.AdminNotificationMessage);
        AdminNotificationSpinner = (TextView) findViewById(R.id.AdminNotificationSpiner);
        notification = findViewById(R.id.notification);

        Intent intent = getIntent();
        String Code = intent.getStringExtra("schoolCode");
        sCode = Code;
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.item_list, android.R.layout.simple_spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    String message = parent.getItemAtPosition(position).toString();
                    AdminNotificationSpinner.setText(message);
                    notification.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String AdminNotificationSender_Name = AdminNotificationSenderName.getText().toString().trim();
                            Calendar calendar = Calendar.getInstance();
                            String AdminNotification_Date = DateFormat.getDateInstance().format(calendar.getTime());
                            String AdminNotification_Time = new SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(new Date());
                            String AdminNotification_Message = AdminNotificationMessage.getText().toString().trim();
                            if (!validateSenderName() | !validateMessage()) {
                                return;
                            } else {
                                String dbName = "TeacherNews";
                                add(dbName, AdminNotificationSender_Name, AdminNotification_Date, AdminNotification_Time, AdminNotification_Message);
                            }
                        }
                    });
                }
                else if(position == 1) {
                    String message = parent.getItemAtPosition(position).toString();
                    AdminNotificationSpinner.setText(message);
                    notification.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String AdminNotificationSender_Name = AdminNotificationSenderName.getText().toString().trim();
                            Calendar calendar = Calendar.getInstance();
                            String AdminNotification_Date = DateFormat.getDateInstance().format(calendar.getTime());
                            String AdminNotification_Time = new SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(new Date());
                            String AdminNotification_Message = AdminNotificationMessage.getText().toString().trim();
                            if (!validateSenderName() | !validateMessage()) {
                                return;
                            }
                            else {
                                String dbName = "ParentNews";
                                add(dbName, AdminNotificationSender_Name, AdminNotification_Date, AdminNotification_Time, AdminNotification_Message);
                            }
                        }
                    });

                }
                else {
                    String message = parent.getItemAtPosition(position).toString();
                    AdminNotificationSpinner.setText(message);
                    notification.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String AdminNotificationSender_Name = AdminNotificationSenderName.getText().toString().trim();
                            String AdminNotification_Message = AdminNotificationMessage.getText().toString().trim();
                            Calendar calendar = Calendar.getInstance();
                            String AdminNotification_Date = DateFormat.getDateInstance().format(calendar.getTime());
                            String AdminNotification_Time = new SimpleDateFormat("hh:mm aa",Locale.getDefault()).format(new Date());
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TeacherNews");
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ParentNews");
                            AdminNotificationHelperClass adminNotificationHelperClass = new AdminNotificationHelperClass(AdminNotificationSender_Name, AdminNotification_Date, AdminNotification_Time, AdminNotification_Message);
                            databaseReference.child(AdminNotification_Time).setValue(adminNotificationHelperClass);
                            reference.child(AdminNotification_Time).setValue(adminNotificationHelperClass);
                            Toast.makeText(getApplicationContext(), "Message has been sent successfully", Toast.LENGTH_SHORT).show();
                            AdminNotificationSenderName.setText("");
                            AdminNotificationMessage.setText("");
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void BackToAdminNotification(View view) {
        Intent intent = new Intent(getApplicationContext(),AdminNotificationActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

    private void add(String dbName, String adminNotificationSender_Name, String adminNotification_Date, String adminNotification_Time, String adminNotification_Message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(dbName);
        AdminNotificationHelperClass adminNotificationHelperClass = new AdminNotificationHelperClass(adminNotificationSender_Name, adminNotification_Date, adminNotification_Time,adminNotification_Message);
        databaseReference.child(adminNotification_Time).setValue(adminNotificationHelperClass);
        Toast.makeText(getApplicationContext(),"Message has been sent successfully",Toast.LENGTH_SHORT).show();
        AdminNotificationSenderName.setText("");
        AdminNotificationMessage.setText("");
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

    private boolean validateSenderName() {
        String val = AdminNotificationSenderName.getText().toString().trim();
        if(val.isEmpty()){
            AdminNotificationSenderName.setError("Field cannot be empty");
            AdminNotificationSenderName.requestFocus();
            return false;
        }
        return true;
    }
}