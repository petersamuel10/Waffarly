package com.example.jesus.waffarly;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jesus.waffarly.Common.Common;
import com.example.jesus.waffarly.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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
    byte[] data;

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

            case R.id.save:
                if(Common.isConnectToTheInternet(getBaseContext())) {
                    name = registerUserName.getText().toString();
                    email = registerEmail.getText().toString();
                    password = registerPassword.getText().toString();

                    try {
                        progress = new ProgressDialog(this);
                        progress.setMessage("Register ...!!!");
                        progress.show();

                        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                            mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    final String userId = mAuth.getCurrentUser().getUid();
                                    Toast.makeText(Register.this, "dd" + userId, Toast.LENGTH_SHORT).show();
                                    final StorageReference storageRef = storageReference.child("images/" + userId);

                                    compressImage();
                                    UploadTask uploadTask = storageRef.putBytes(data);
                                    Toast.makeText(Register.this, "" + data.toString(), Toast.LENGTH_SHORT).show();

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            String uri = taskSnapshot.getDownloadUrl().toString();

                                            final User user = new User(name, userId, uri.toString());
                                            database.child(userId).setValue(user);

                                            progress.dismiss();
                                            Toast.makeText(Register.this, "Register Completed Successfully", Toast.LENGTH_LONG).show();

                                            startActivity(new Intent(Register.this, MainActivity.class));
                                            finish();
                                        }
                                    });

                                }
                            });

                        } else
                            Toast.makeText(this, "Please fill full Information !!!", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {

                        progress.dismiss();
                        Toast.makeText(getApplication(), "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }else
                    Toast.makeText(getBaseContext(), "Please Check Ihe Internet Connection !!!", Toast.LENGTH_SHORT).show();
            break;
            case R.id.profile:
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"choose profile pic"),PICK_IMAGE);
                break;
            default:
            case R.id.cancel:
                startActivity(new Intent(getApplication(),Login.class));
                finish();
                break;
        }
    }

    private void compressImage() {

        //compress image

        profileImage.setDrawingCacheEnabled(true);
        profileImage.buildDrawingCache();
        Bitmap bitmap = profileImage.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        data = byteArrayOutputStream.toByteArray();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
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
        database = FirebaseDatabase.getInstance().getReference("Users");

    }

}

