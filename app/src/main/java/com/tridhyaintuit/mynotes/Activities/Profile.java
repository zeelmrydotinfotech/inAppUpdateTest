package com.tridhyaintuit.mynotes.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rydotinfotech.mynotes.R;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    ImageView profileImg;
    Button uploadProfileImg;
    ProgressBar progressBar;

    Uri imageUri;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        storageReference = FirebaseStorage.getInstance().getReference(user.getEmail());

        profileImg = findViewById(R.id.imgProfilePicker);
        uploadProfileImg = findViewById(R.id.btnUploadImg);
        progressBar = findViewById(R.id.progress);

        StorageReference getProfileImage = storageReference.child("user profile");


        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgPicker();
            }
        });

        uploadProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void imgPicker() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void uploadImage() {
        uploadProfileImg.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);
        StorageReference reference = storageReference.child("user profile");
        reference.putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Profile.this,"Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this,"Sorry, Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profileImg);
        }

    }
}