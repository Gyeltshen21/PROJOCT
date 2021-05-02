package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminProfileActivity extends AppCompatActivity {

    private ImageView AdminProfilePic;
    private Context context = AdminProfileActivity.this;
    private static final int PICK_ADMIN_IMAGE_REQUEST = 1;
    private Uri adminImageUri;
    private ProgressBar AdminProgressBar;
    private TextView AdminHeaderName, AdminName, AdminSchoolCode, AdminEmail, AdminPhoneNo;
    private DatabaseReference databaseReference;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        AdminProgressBar = (ProgressBar) findViewById(R.id.AdminProgressBar);
        AdminProfilePic = (ImageView) findViewById(R.id.AdminProfilePic);
        AdminHeaderName = (TextView) findViewById(R.id.AdminHeaderName);
        AdminName = (TextView) findViewById(R.id.AdminName);
        AdminSchoolCode = (TextView) findViewById(R.id.AdminCode);
        AdminEmail = (TextView) findViewById(R.id.AdminEmail);
        AdminPhoneNo = (TextView) findViewById(R.id.AdminPhoneNo);

        Intent intent = getIntent();
        String schoolCode = intent.getStringExtra("schoolCode");
        sCode = schoolCode;
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = databaseReference.orderByChild("schoolCode").equalTo(sCode);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    AdminSchoolCode.setError(null);
                    AdminSchoolCode.setEnabled(false);
                    String nameDB = snapshot.child(schoolCode).child("name").getValue(String.class);
                    String emailDB = snapshot.child(schoolCode).child("email").getValue(String.class);
                    String phoneDB = snapshot.child(schoolCode).child("phone").getValue(String.class);
                    String schoolCodeDB = snapshot.child(schoolCode).child("schoolCode").getValue(String.class);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("users/"+sCode+"/image.jpg");
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(context).load(uri).into(AdminProfilePic);
                        }
                    });
                    AdminHeaderName.setText(nameDB);
                    AdminName.setText(nameDB);
                    AdminSchoolCode.setText(schoolCodeDB);
                    AdminEmail.setText(emailDB);
                    AdminPhoneNo.setText(phoneDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoBackToHome(View view) {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("schoolCode",sCode);
        startActivity(intent);
    }
    //Button to Choose photo
    public void ChooseAdminProfilePhoto(View view) {
        openAdminFileChooser();
    }
    //Method to choose file
    private void openAdminFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_ADMIN_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_ADMIN_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            adminImageUri = data.getData();
            Picasso.with(this).load(adminImageUri).into(AdminProfilePic);
        }
    }

    //Button to Upload photo
    public void UploadAdminProfilePhoto(View view) {
        UploadFile();
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void UploadFile() {
        if(adminImageUri != null){
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference("users/"+sCode+"/image.jpg");
            fileReference.putFile(adminImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AdminProgressBar.setProgress(0);
                        }
                    },500);
                    Toast.makeText(getApplicationContext(),"Uploaded Successfully", Toast.LENGTH_LONG).show();
//                    AdminProfilePhoto upload = new AdminProfilePhoto(taskSnapshot.getUploadSessionUri().toString());
//                    String adminUploadId = databaseReference.push().getKey();
//                    databaseReference.child(sCode).child("image").setValue(upload);
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
                    AdminProgressBar.setProgress((int) progress);
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }
}