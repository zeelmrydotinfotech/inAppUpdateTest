package com.tridhyaintuit.mynotes.Activities;

//import static com.tridhyaintuit.mynotes.Constants.Flexible.FLEXIBLE_APP_UPDATE_REQ_CODE;

import static com.tridhyaintuit.mynotes.Constants.Flexible.IMMEDIATE_APP_UPDATE_REQ_CODE;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tridhyaintuit.mynotes.R;

public class Login extends AppCompatActivity {

    EditText regEmail, regPassword;
    Button btnLogin, btnLoginWithPhone;
    TextView tvSignup, tvForgotPass;

    FirebaseAuth loginAuth = FirebaseAuth.getInstance();

    AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appUpdateManager = AppUpdateManagerFactory.create(this);
        checkUpdate();

        regEmail = findViewById(R.id.etEmailId);
        regPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignUpHere);
        tvForgotPass = findViewById(R.id.tvForgotPass);
        btnLoginWithPhone = findViewById(R.id.btnLoginWithPhone);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = regEmail.getText().toString();
                String password = regPassword.getText().toString();
                firebaseAuthLogin(mail, password);
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                String mail = regEmail.getText().toString();
                intent.putExtra("ResetEmail", mail);
                startActivity(intent);
            }
        });

        btnLoginWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, LoginWIthPhone.class);
                startActivity(intent);
            }
        });

    }

    private void firebaseAuthLogin(String mail, String password) {

        loginAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this, "Logged in successfully",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, NoteList.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = loginAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(Login.this, NoteList.class);
            startActivity(intent);
            finish();
        }
    }

   /* Flexible app update
   private void checkUpdate() {
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                startUpdateFlow(appUpdateInfo);
                Log.d("UPDATE", "Update available");
            } else {
                Log.d("UPDATE", "No Update available");
                popupSnackBarForCompleteUpdate();
            }
        });
    }*/

    //IMMEDIATE app update.
    private void checkUpdate() {

        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            /*appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this,
                    FLEXIBLE_APP_UPDATE_REQ_CODE);*/
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this,
                    IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*Flexible app update
        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {*/

        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }

    }

   /*Flexible app update
   InstallStateUpdatedListener installStateUpdatedListener = state -> {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackBarForCompleteUpdate();
        } else if (state.installStatus() == InstallStatus.INSTALLED) {
            removeInstallStateUpdateListener();
        } else {
            Toast.makeText(getApplicationContext(), "InstallStateUpdatedListener: state: " + state.installStatus(), Toast.LENGTH_LONG).show();
        }
    };

    private void popupSnackBarForCompleteUpdate() {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New app is ready!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Install", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (appUpdateManager != null) {
                            appUpdateManager.completeUpdate();
                        }
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.purple_500))
                .show();
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
    }
}