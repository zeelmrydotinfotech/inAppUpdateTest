package com.tridhyaintuit.mynotes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rydotinfotech.mynotes.R;

import java.util.concurrent.TimeUnit;

public class LoginWIthPhone extends AppCompatActivity {

    EditText phoneNumber, enterOtp;
    Button sendOtp, verifyOtp;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String sentOtpCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_phone);

        phoneNumber = findViewById(R.id.etMobileNumber);
        enterOtp = findViewById(R.id.etOtp);
        sendOtp = findViewById(R.id.btnSendOtp);
        verifyOtp = findViewById(R.id.btnLoginWithOtp);

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = phoneNumber.getText().toString();
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(90L, TimeUnit.SECONDS)
                        .setActivity(LoginWIthPhone.this)
                        .setCallbacks(mCallbacks)
                        .build();

                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signWithOtpCode();
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e("Failed", e.toString());

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            sentOtpCode = s;
        }
    };

    public void signWithOtpCode(){
        String enteredUserCode = enterOtp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(sentOtpCode, enteredUserCode);
        signInWithPhoneAuthCredential(credential);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginWIthPhone.this, NoteList.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginWIthPhone.this, "Otp Verification is failed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}