package com.example.jesus.waffarly;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity implements View.OnClickListener {
    CircleImageView profileImage;
    EditText registerUserName;
    EditText registerEmail;
    EditText registerPassword ;
    Button createAccount;
    Button backToLogin;
    ProgressDialog progress;
    private static final int PICK_IMAGE =1 ;
    private static Uri profileUri;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private DatabaseReference database;
    public String name;
    public String email;
    public String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //references
        reference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //choose profile picture
        profileImage.setOnClickListener(this);
        //register button
        createAccount.setOnClickListener(this);
        //back to login page
        backToLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.save: {

                name = registerUserName.getText().toString();
                email = registerEmail.getText().toString();
                password = registerPassword.getText().toString();

                try {

                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                         progress = ProgressDialog.show(getApplication(),"Add offer","progress...");
                       // Toast.makeText(Register.this, name + email + password + profile_Uri.toString(), Toast.LENGTH_LONG).show();
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    final String user_id = mAuth.getCurrentUser().getUid();

                                    StorageReference ref = storageReference.child("images/" + user_id + ".jpg");
                                    ref.putFile(profileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                            progress.dismiss();
                                            Toast.makeText(Register.this, "Register Completed Successfully", Toast.LENGTH_LONG).show();
                                            String image_uri = task.getResult().getDownloadUrl().toString();

                                            HashMap<String, Object> user = new HashMap<>();
                                            user.put("name", name);
                                            user.put("id", user_id);
                                            user.put("image_uri", image_uri);

                                            database.child("Users").child(user_id).setValue(user);

                                            Toast.makeText(Register.this, "datttttttta", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Register.this, MainActivity.class));
                                            finish();
                                        }
                                    });
                                }
                                }
                        });
                    }

                } catch (Exception e) {
                    progress.dismiss();
                    Toast.makeText(getApplication(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            case R.id.cancel:
                startActivity(new Intent(getApplication(),Login.class));
                finish();
                break;

            case R.id.profile:
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"choose profile pic"),PICK_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE)
        {
            profileUri = data.getData();
            profileImage.setImageURI(profileUri);
        }
    }

    private void reference() {
        profileImage = findViewById(R.id.profile);
        registerUserName =findViewById(R.id.name);
        registerEmail =findViewById(R.id.email);
        registerPassword = findViewById(R.id.password);
        createAccount = findViewById(R.id.save);
        backToLogin = findViewById(R.id.cancel);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference();
    }

}
