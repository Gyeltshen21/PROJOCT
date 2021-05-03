package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AdminPDFActivity extends AppCompatActivity {

    private ImageView AdminPDFUpload;
    private Button AdminPDFBtn, AdminPDFChooseFile;
    private EditText AdminPDFName;
    private ProgressBar AdminPDFProgressBar;
    private DatabaseReference databaseReference;
    private Uri AdminPDFFile;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_p_d_f);
        //Receive data from home activity
        Intent intent = getIntent();
        String schoolCode = intent.getStringExtra("schoolCode");
        sCode = schoolCode;

        //Linking id with xml
        AdminPDFUpload = (ImageView) findViewById(R.id.AdminPDFUpload);
        AdminPDFName = (EditText) findViewById(R.id.AdminPDFName);
        AdminPDFBtn = (Button) findViewById(R.id.AdminPDFBtn);
        AdminPDFChooseFile = (Button) findViewById(R.id.AdminPDFChooseFileBtn);
        AdminPDFProgressBar = (ProgressBar) findViewById(R.id.AdminPDFProgressBar);

        //Connecting with firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        
        AdminPDFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPDFFile();
            }
        });

        AdminPDFChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });
    }
    //Allow user to select pdf file
    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
    }

    //Check weather user selected pdf file or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            AdminPDFFile = data.getData();
            Picasso.with(this).load(AdminPDFFile).into(AdminPDFUpload);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //Upload file to storage
    private void uploadPDFFile() {
        if(AdminPDFFile != null){
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference("AdminPDF/" +System.currentTimeMillis() + "." +getFileExtension(AdminPDFFile));
            fileReference.putFile(AdminPDFFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AdminPDFProgressBar.setProgress(0);
                        }
                    },500);
                    AdminPDFHelperClass upload = new AdminPDFHelperClass(AdminPDFName.getText().toString().trim(),taskSnapshot.getUploadSessionUri().toString());
                    String adminUploadId = databaseReference.push().getKey();
                    databaseReference.child(sCode).child("PDF").child(adminUploadId).setValue(upload);
                    AdminPDFName.setText("");
                    Toast.makeText(getApplicationContext(),"Uploaded Successfully", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    AdminPDFProgressBar.setProgress((int) progress);
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    //Back to Home Activity
    public void BackToHome(View view) {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }
}