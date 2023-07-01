package com.tridhyaintuit.mynotes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rydotinfotech.mynotes.R;

public class ForgotPassword extends AppCompatActivity {

    EditText resetPass;
    Button btnResetPass;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetPass = findViewById(R.id.etResetPass);
        btnResetPass = findViewById(R.id.btnResetPass);

        Intent intent = getIntent();
        String LoggedInEmail = intent.getStringExtra("ResetEmail");
        resetPass.setText(LoggedInEmail);

        String email = resetPass.getText().toString();

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPassword.this, "Password is changed successfully",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ForgotPassword.this, Login.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                    finish();
                }
        });
    }
}