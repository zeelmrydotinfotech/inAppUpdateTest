package com.tridhyaintuit.mynotes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tridhyaintuit.mynotes.R;

public class SignUp extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnSignUp;
    TextView tvLogin;

    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSigUp);
        tvLogin = findViewById(R.id.tvLoginHere);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                firebaseSignUpAuth(mail, password);

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void firebaseSignUpAuth(String mail, String password) {

        auth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUp.this, "Account is created successfully",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("AAA", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "There is a problem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}