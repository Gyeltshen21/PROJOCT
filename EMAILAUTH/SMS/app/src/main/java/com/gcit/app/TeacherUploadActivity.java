package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class TeacherUploadActivity extends AppCompatActivity {

    ImageView TeacherImageBrowser, TeacherImageUpload, TeacherFileLogo, TeacherFileCancel;
    Uri TeacherFilePath;
    EditText TeacherFileName;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_upload);

        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        sCode = employeeID;

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("TeacherPDFResult");

        TeacherFileName = (EditText) findViewById(R.id.TeacherUploadFileName);

        TeacherImageBrowser = findViewById(R.id.TeacherUploadImageBrowse);
        TeacherImageUpload = findViewById(R.id.TeacherUploadImageUpload);
        TeacherFileLogo = findViewById(R.id.TeacherUploadFileLogo);
        TeacherFileCancel = findViewById(R.id.TeacherUploadCancelFile);
        TeacherFileLogo.setVisibility(View.INVISIBLE);
        TeacherFileCancel.setVisibility(View.INVISIBLE);

        TeacherFileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherFileLogo.setVisibility(View.INVISIBLE);
                TeacherFileCancel.setVisibility(View.INVISIBLE);
                TeacherImageBrowser.setVisibility(View.VISIBLE);
            }
        });

        TeacherImageBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select PDF Files"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        TeacherImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processUpload(TeacherFilePath);
            }
        });
    }

    private void processUpload(Uri teacherFilePath) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.show();
        if(teacherFilePath != null){
            StorageReference reference = storageReference.child("TeacherPDFResult/" +System.currentTimeMillis() + ".pdf");
            reference.putFile(teacherFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    TeacherPDFHelperClass teacherPDFHelperClass = new TeacherPDFHelperClass(TeacherFileName.getText().toString().trim(),uri.toString());
                                    databaseReference.child(databaseReference.push().getKey()).setValue(teacherPDFHelperClass);
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"File Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                    TeacherFileLogo.setVisibility(View.INVISIBLE);
                                    TeacherFileCancel.setVisibility(View.INVISIBLE);
                                    TeacherImageBrowser.setVisibility(View.VISIBLE);
                                    TeacherFileName.setText("");
                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent = (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressDialog.setMessage("File Uploading...." + (int)percent + "%");
                        }
                    });
        }
        else{
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){
            TeacherFilePath = data.getData();
            TeacherFileLogo.setVisibility(View.VISIBLE);
            TeacherFileCancel.setVisibility(View.VISIBLE);
            TeacherImageBrowser.setVisibility(View.INVISIBLE);
        }
    }

    public void BackToTeacherPDFList(View view) {
        Intent intent = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        intent.putExtra("employeeID",sCode);
        startActivity(intent);
    }
}