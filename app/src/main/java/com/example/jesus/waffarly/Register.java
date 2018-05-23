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
    CircleImageView profile;
    EditText register_user_name;
    EditText register_email;
    EditText register_password ;
    Button create_account;
    Button back_to_login;
    ProgressDialog progress;
    private static final int PICK_IMAGE =1 ;
    private static Uri profile_Uri;
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
        profile.setOnClickListener(this);
        //register button
        create_account.setOnClickListener(this);
        //back to login page
        back_to_login.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.save: {

                name = register_user_name.getText().toString();
                email = register_email.getText().toString();
                password = register_password.getText().toString();

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
                                    ref.putFile(profile_Uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
            profile_Uri = data.getData();
            profile.setImageURI(profile_Uri);
        }
    }

    private void reference() {
        profile = (CircleImageView)findViewById(R.id.profile);
        register_user_name =(EditText)findViewById(R.id.name);
        register_email =(EditText)findViewById(R.id.email);
        register_password =(EditText)findViewById(R.id.password);
        create_account = (Button)findViewById(R.id.save);
        back_to_login = (Button)findViewById(R.id.cancel);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference();
    }

}
