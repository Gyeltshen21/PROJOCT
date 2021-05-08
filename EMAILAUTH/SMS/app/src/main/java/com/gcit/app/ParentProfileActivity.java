package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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

public class ParentProfileActivity extends AppCompatActivity {
    
    private ImageView ParentProfilePic;
    private Context context = ParentProfileActivity.this;
    private static final int PICK_Parent_IMAGE_REQUEST = 1;
    private Uri ParentImageUri;
    private DatabaseReference databaseReference;
    private TextView parentHeaderName, parentName, parentStdCode, parentEmail, parentPhoneNo;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        ParentProfilePic = (ImageView) findViewById(R.id.ParentProfilePic);
        parentHeaderName = (TextView) findViewById(R.id.parentHeaderName1);
        parentName = (TextView) findViewById(R.id.parentName1);
        parentStdCode = (TextView) findViewById(R.id.parentStdCode1);
        parentEmail = (TextView) findViewById(R.id.parentEmail1);
        parentPhoneNo = (TextView) findViewById(R.id.parentPhoneNo1);
        Intent intent = getIntent();
        String stdCode = intent.getStringExtra("stdCode");
        sCode = stdCode;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("parent");
        Query checkUser = databaseReference.orderByChild("stdCode").equalTo(stdCode);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    parentStdCode.setError(null);
                    parentStdCode.setEnabled(false);
                    String nameDB = snapshot.child(stdCode).child("name").getValue(String.class);
                    String emailDB = snapshot.child(stdCode).child("email").getValue(String.class);
                    String phoneDB = snapshot.child(stdCode).child("phone").getValue(String.class);
                    String stdCodeDB = snapshot.child(stdCode).child("stdCode").getValue(String.class);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("parent/"+sCode+"/image.jpg");
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(context).load(uri).into(ParentProfilePic);
                        }
                    });
                    parentHeaderName.setText(nameDB);
                    parentName.setText(nameDB);
                    parentStdCode.setText(stdCodeDB);
                    parentEmail.setText(emailDB);
                    parentPhoneNo.setText(phoneDB);
                }
            }
            //Data fetch error
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Back button to home
    public void GoBackToParentHome(View view) {
        Intent intent = new Intent(getApplicationContext(),ParentHomeActivity.class);
        intent.putExtra("stdCode",sCode);
        startActivity(intent);
    }


    //Button to Choose photo
    public void ChooseParentProfilePhoto(View view) {
        openParentFileChooser();
    }
    //Method to choose file
    private void openParentFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_Parent_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_Parent_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            ParentImageUri = data.getData();
            Picasso.with(this).load(ParentImageUri).into(ParentProfilePic);
        }
    }

    //Button to Upload photo
    public void UploadParentProfilePhoto(View view) {
        UploadFile();
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void UploadFile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        if(ParentImageUri != null){
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference("parent/"+sCode+"/image.jpg");
            fileReference.putFile(ParentImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.setProgress(0);
                        }
                    },500);
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Uploaded Successfully", Toast.LENGTH_LONG).show();
//                    ParentProfilePhoto upload = new ParentProfilePhoto(taskSnapshot.getUploadSessionUri().toString());
//                    String ParentUploadId = databaseReference.push().getKey();
//                    databaseReference.child(sCode).child("image").setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading..." + (int) progress + "%");
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
        }
    }
}