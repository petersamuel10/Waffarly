package com.example.jesus.waffarly;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jesus.waffarly.Common.Common;
import com.example.jesus.waffarly.Model.Offer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import im.delight.android.location.SimpleLocation;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;

public class AddOffer extends Fragment implements View.OnClickListener {
    private View v;
    private ImageView imageView;
    private ImageView location,locationChecked;
    private Spinner category;
    private EditText nameEt;
    private EditText summaryEt;
    private EditText descriptionEt;
    private EditText addressEt;
    private Button add;
    private String name;
    private String description;
    private String address;
    private String summary;
    private String categoryName;
    private Uri imageViewLink = null;
    private String longitude;
    private String latitude;
    private ProgressDialog progressDialog;


    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    byte[] data;

    private SimpleLocation mLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.add__offer, container, false);

        //reference;
        reference();
        // set spinner
        spinner();

        //get data from UI
        categoryName = category.getSelectedItem().toString();
        name = nameEt.getText().toString();
        description = descriptionEt.getText().toString();
        summary = summaryEt.getText().toString();
        address = addressEt.getText().toString();
        mLocation = new SimpleLocation(getContext());

        if(Common.isConnectToTheInternet(getContext())) {
        //    if (!mLocation.hasLocationEnabled())
        //      SimpleLocation.openSettings(getContext());
        // set OnClick Actions
        imageView.setOnClickListener(this);
        location.setOnClickListener(this);
        add.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Offer");
    }else
        Toast.makeText(getContext(), "Please Check Ihe Internet Connection !!!", Toast.LENGTH_SHORT).show();

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.offer_image:
                //open dialog to get picture from gallery or open camera
                selectPicture();
                break;
            case R.id.location:
                //get Longitude and Latitude for the company location
                getLocation();
                break;
            default:
                // add offer to the database
                saveOffer();
                break;
        }

    }

    private void saveOffer() {
        if (imageViewLink == null) {
            Toast.makeText(this.getContext(), "Please select a picture", Toast.LENGTH_LONG).show();
        } else {
            //get data from UI
            categoryName = category.getSelectedItem().toString();
            name = nameEt.getText().toString();
            description = descriptionEt.getText().toString();
            summary = summaryEt.getText().toString();
            address = addressEt.getText().toString();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(summary) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(address)) {
                try {
                    progressDialog = ProgressDialog.show(getContext(), "Add offer", "progress...");
                    StorageReference offer_image = storageReference.child("OffersImages/" + categoryName + "/" + imageViewLink.getLastPathSegment() + ".jpg");

                    compressImage();
                    UploadTask uploadTask = offer_image.putBytes(data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String random = String.valueOf(new Random().nextInt(10000 - 3) + 3);
                            String uri = taskSnapshot.getDownloadUrl().toString();
                            Offer offer = new Offer(name, address, description, uri, latitude, longitude, categoryName, summary, random);
                            ref.child(random).setValue(offer);
                            Clear();
                            progressDialog.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + progress + " %");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Please Check your Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(this.getContext(), "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                }
            } else
                Toast.makeText(this.getContext(), "Please fill missed field", Toast.LENGTH_LONG).show();
        }


    }

    private void Clear() {
        nameEt.setText("");
        addressEt.setText("");
        descriptionEt.setText("");
        summaryEt.setText("");
        name = null;
    }

    //get Longitude and Latitude for the company location
    private void getLocation() {

        requestPermission();
        if (!mLocation.hasLocationEnabled())
            SimpleLocation.openSettings(getContext());
        try {
            longitude = String.valueOf(mLocation.getLongitude());
            latitude = String.valueOf(mLocation.getLatitude());
            Toast.makeText(getContext(), "Failed 1111111111111"+latitude, Toast.LENGTH_SHORT).show();

            mLocation.setListener(new SimpleLocation.Listener() {
                @Override
                public void onPositionChanged() {
                    mLocation = new SimpleLocation(getContext());

                    longitude = String.valueOf(mLocation.getLongitude());
                    latitude = String.valueOf(mLocation.getLatitude());
                    Toast.makeText(getContext(), "Fail55555555555555"+latitude, Toast.LENGTH_SHORT).show();
                }
            });
            if (mLocation == null)
                Toast.makeText(getContext(), "Failed to find the location", Toast.LENGTH_SHORT).show();
            else{
                longitude = String.valueOf(mLocation.getLongitude());
                latitude = String.valueOf(mLocation.getLatitude());
                locationChecked.setVisibility(View.VISIBLE);
            }

            Toast.makeText(getContext(), ""+latitude, Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //open dialog to get picture from gallery or open camera
    private void selectPicture() {
        String[] items = {"select picture from gallery", "take Picture"};
        AlertDialog.Builder takePicture = new AlertDialog.Builder(this.getActivity());
        takePicture.setTitle("Choose Picture");
        takePicture.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i==0) {
                    // select image from Gallery
                    startActivityForResult(new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 0);
                }else {
                    // capture new picture
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 1);

                    }
                }                }
        });
        takePicture.show();
    }

// result activity for taking pic from gallery or camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0) {
            if (resultCode == RESULT_OK) {
                imageViewLink = data.getData();
                imageView.setImageURI(imageViewLink);
            }
        }else {

            if (resultCode == RESULT_OK) {
                try {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    // save capture picture to internal storage and get the Uri
                    imageViewLink = getImageURI(this.getContext(), thumbnail);
                    imageView.setImageURI(imageViewLink);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "eEEee" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    // get uri from Bitmap image captured from camera
    private Uri getImageURI(Context context, Bitmap thumbnail) {
        String path=null;
        try {

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
            path = MediaStore.Images.Media.insertImage(context.getContentResolver(), thumbnail, "title", null);

        } catch (Exception e) {
            Toast.makeText(this.getContext(),"ERROR : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return Uri.parse(path);
    }
    private void compressImage() {

        //compress image

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        data = byteArrayOutputStream.toByteArray();
    }

    private void spinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        // the third argument is to specify which layout to show selected choice in spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.category, android.R.layout.simple_spinner_item);
        // specify how tho show adapter items
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        category.setAdapter(adapter);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_COARSE_LOCATION},1);
        ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION},5);
    }
    private void reference() {
        category = v.findViewById(R.id.category);
        imageView = v.findViewById(R.id.offer_image);
        nameEt = v.findViewById(R.id.name);
        addressEt = v.findViewById(R.id.address);
        descriptionEt = v.findViewById(R.id.description);
        summaryEt =v.findViewById(R.id.summary);
        location = v.findViewById(R.id.location);
        locationChecked = v.findViewById(R.id.locationAdded);
        add = v.findViewById(R.id.add);
        storageReference = FirebaseStorage.getInstance().getReference();
    }

}