package com.tridhyaintuit.mynotes.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tridhyaintuit.mynotes.R;
import com.tridhyaintuit.mynotes.models.AppDatabase.ProfileData;
import com.tridhyaintuit.mynotes.models.AppDatabase.ProfileViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class Profile extends AppCompatActivity {

    ImageView profileImg;
    Button uploadProfileImg;
    ProgressBar progressBar;

    Uri imageUri;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    StorageReference storageReference;
    ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        storageReference = FirebaseStorage.getInstance().getReference(user.getEmail());

        profileImg = findViewById(R.id.imgProfilePicker);
        uploadProfileImg = findViewById(R.id.btnUploadImg);
        progressBar = findViewById(R.id.progress);

        StorageReference getProfileImage = storageReference.child("user profile");

         profileViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(ProfileViewModel.class);

        profileViewModel.getAllProfile().observe(this, new Observer<List<ProfileData>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(List<ProfileData> profileData) {
                String encoded = Base64.getEncoder().encodeToString(profileData.get(0).getImageUrl());
                String decoded = new String(Base64.getDecoder().decode(encoded.getBytes()));
                Uri uri = Uri.parse(decoded);
                Picasso.get().load(uri).into(profileImg);
                Log.d("ProfileUrl", String.valueOf(uri));
            }
        });
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
        if (imageUri != null) {
            final byte[][] inputData = new byte[1][1];
            uploadProfileImg.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            StorageReference reference = storageReference.child("user profile");
            reference.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            //convert uri to byte and pass it in profileData...
                            try {
                                InputStream iStream =   getContentResolver().openInputStream(imageUri);
                                inputData[0] = getBytes(iStream);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            ProfileData profileData = new ProfileData(0, inputData[0]);
                            profileViewModel.insert(profileData);
                            Toast.makeText(Profile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, "Sorry, Something went wrong", Toast.LENGTH_SHORT).show();

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
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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