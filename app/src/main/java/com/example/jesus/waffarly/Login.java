package com.example.jesus.waffarly;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText login_email;
    EditText login_password;
    Button login;
    TextView register;
    public FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //references
        reference();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),Register.class));
            finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser current_user = mAuth.getCurrentUser();
        if(current_user!=null)
            goToMyProfile();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progress = ProgressDialog.show(getApplication(),"Add offer","progress...");
                String email = login_email.getText().toString();
                String password = login_password.getText().toString();

                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)) {
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                goToMyProfile();
                            }else
                            {
                                Toast.makeText(Login.this,"Error "+task.getException().getMessage(),Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }else
                {
                    Toast.makeText(Login.this,"Error : Please inter your email and password ",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void goToMyProfile() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void reference() {
        login_email=(EditText)findViewById(R.id.log_email);
        login_password=(EditText)findViewById(R.id.log_password);
        login=(Button)findViewById(R.id.login);
        register=(TextView)findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();

    }
}
