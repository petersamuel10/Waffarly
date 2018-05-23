package com.example.jesus.waffarly;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.builders.StickerBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.concurrent.Executor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;

public class Add_Offer extends Fragment implements View.OnClickListener {
    View v;
    ImageView imageView;
    ImageView location;
    Spinner category;
    EditText nameEt;
    EditText summaryEt;
    EditText descriptionEt;
    EditText addressEt;
    Button add;
    String name;
    String description;
    String address;
    String summary;
    String categoryName;
    Uri imageViewLink = null;
    String longitude;
    String latitude;
    ProgressDialog progress;

    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    static final int REQUEST_LOCATION = 2;
    private LocationManager locationManager;
    private LocationListener locationListener;

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

        // set OnClick Actions
        imageView.setOnClickListener(this);
        getLocation();
        add.setOnClickListener(this);

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

            case R.id.add:
                // add offer to the database
                saveOffer();
                break;
        }

    }

    private void saveOffer() {
        if (imageViewLink != null) {

            //get data from UI
            categoryName = category.getSelectedItem().toString();
            name = nameEt.getText().toString();
            description = descriptionEt.getText().toString();
            summary = summaryEt.getText().toString();
            address = addressEt.getText().toString();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(summary) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(address)) {
                try {
                    progress = ProgressDialog.show(getContext(), "Add offer", "progress...");
                    StorageReference offer_image = storageReference.child("OffersImages/" + categoryName + "/" + imageViewLink.getLastPathSegment() + ".jpg");
                    offer_image.putFile(imageViewLink).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            String image_uri = task.getResult().getDownloadUrl().toString();
                            HashMap<String, Object> offer_details = new HashMap<>();
                            offer_details.put("image", image_uri);
                            offer_details.put("name", name);
                            offer_details.put("description", description);
                            offer_details.put("summary", summary);
                            offer_details.put("address", address);

                            firestore.collection(categoryName).document(name).set(offer_details).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progress.dismiss();
                                    Toast.makeText(getContext(), "Added Success", Toast.LENGTH_LONG).show();
                                    // clear handle fields
                                    Clear();
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(this.getContext(), "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progress.dismiss();

                }
            } else
                Toast.makeText(this.getContext(), "Please fill missed field", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this.getContext(), "Please select a picture", Toast.LENGTH_LONG).show();


    }

    private void Clear() {
        Toast.makeText(this.getContext(), "dooooone", Toast.LENGTH_LONG).show();
        nameEt.setText("");
        addressEt.setText("");
        descriptionEt.setText("");
        summaryEt.setText("");
        name = null;
    }

    //get Longitude and Latitude for the company location
    private void getLocation() {


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                longitude = String.valueOf(location.getLongitude());
                Toast.makeText(getContext(), longitude, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            return;
        } else {
            ConfigureButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ConfigureButton();
                return;
        }
    }

    private void ConfigureButton() {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
            }
        });
    }


    //open dialog to get picture from gallery or open camera
    private void selectPicture() {
        String[] items = {"select picture from gallery", "take Picture"};
        AlertDialog.Builder takePicture = new AlertDialog.Builder(this.getActivity());
        takePicture.setTitle("Choose Picture");
        takePicture.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i) {
                    case 0:
                        // select image from Gallery
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 0);
                        break;

                    case 1:
                        // capture new picture
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent,1);

                        }
                        //startActivityForResult(intent,CAMERA_REQUEST_CODE);

                        break;
                }
            }
        });
        takePicture.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    imageViewLink = data.getData();
                    imageView.setImageURI(imageViewLink);
                }
                break;
            case 1:

                if (resultCode == RESULT_OK) {
                    try {
                          Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                         // save capture picture to internal storage and get the Uri
                          imageViewLink = getImageURI(this.getContext(),thumbnail);
                          imageView.setImageURI(imageViewLink);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "eEEee" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    // get uri from Bitmap image captured from camera
    private Uri getImageURI(Context context, Bitmap thumbnail) {
        String path=null;
        try {

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(context.getContentResolver(), thumbnail, "title", null);

        } catch (Exception e) {
            Toast.makeText(this.getContext(), "mmmmm " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return Uri.parse(path);
    }

    private void spinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        // the third argument is to specify which layout to show selected choice in spinnner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.category, android.R.layout.simple_spinner_item);
        // specify how tho show adapter items
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        category.setAdapter(adapter);
    }

    private void reference() {
        category = (Spinner) v.findViewById(R.id.category);
        category.setPrompt("Choose Category");
        imageView = (ImageView) v.findViewById(R.id.offer_image);
        nameEt = (EditText) v.findViewById(R.id.name);
        addressEt = (EditText) v.findViewById(R.id.address);
        descriptionEt = (EditText) v.findViewById(R.id.description);
        summaryEt = (EditText) v.findViewById(R.id.summary);
        location = (ImageView) v.findViewById(R.id.location);
        add = (Button) v.findViewById(R.id.add);
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

}