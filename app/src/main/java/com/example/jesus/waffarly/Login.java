package com.example.jesus.waffarly;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jesus.waffarly.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText loginEmail;
    private EditText loginPassword;
    private Button login;
    private TextView register;
    public FirebaseAuth mAuth;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //references
        reference();
        if(Common.isConnectToTheInternet(getBaseContext())) {
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplication(), Register.class));
                    finish();
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progress = ProgressDialog.show(Login.this, "Login", "progress...");
                    String email = loginEmail.getText().toString();
                    String password = loginPassword.getText().toString();

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    progress.dismiss();
                                    goToMyProfile();
                                } else {
                                    progress.dismiss();
                                    Toast.makeText(Login.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        progress.dismiss();
                        Toast.makeText(Login.this, "Error : Please Enter your email and password ", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        else
            Toast.makeText(getBaseContext(), "Please Check Ihe Internet Connection !!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser current_user = mAuth.getCurrentUser();
        if(current_user!=null)
            goToMyProfile();
    }

    private void goToMyProfile() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void reference() {
        loginEmail=findViewById(R.id.log_email);
        loginPassword=findViewById(R.id.log_password);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();

    }
}
