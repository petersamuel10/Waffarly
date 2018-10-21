package com.example.jesus.waffarly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jesus.waffarly.Model.Offer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class ItemDetails extends AppCompatActivity implements View.OnClickListener , RatingDialogListener{

    private ImageView image;
    private TextView description;
    private TextView address;
    private ImageView location;
    private TextView name;
    private Float longitude = 0.0f;
    private Float latitude = 0.0f;
    private CollapsingToolbarLayout collapse;
    private FloatingActionButton btnRating;
    private RatingBar ratingBar;
    private TextView value;

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private DatabaseReference ratingRef;
    private Offer model;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item__details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this line shows back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //reference
        reference();

        key = getIntent().getStringExtra("key");
        loadDetails(key);

        getRatingValve();
        location.setOnClickListener(this);
        btnRating.setOnClickListener(this);
    }

    private void getRatingValve() {

        Query query = ratingRef.orderByChild("productId").equalTo(key);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                com.example.jesus.waffarly.Model.Rating rating = new com.example.jesus.waffarly.Model.Rating();
                int sum = 0;
                int count = 0;
                float average=0.0f;
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    rating = dataSnapshot1.getValue(com.example.jesus.waffarly.Model.Rating.class);
                    sum+= Integer.parseInt(rating.getValue());
                    count++;
                }
                Log.e("sum",String.valueOf(count));
                if(count!=0)
                {
                    average = (float) sum/count;
                    ratingBar.setRating(average);
                }
                Log.e("sum",String.valueOf(average));
                String [] ratingValue = {"Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"};
               if (average>0) {
                  average = average - 1;
                   value.setText(String.format("%.1f  %s",(average+1),ratingValue[(int)Math.round(average)]));
               }else
                   value.setText("0.0");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
   }

    private void loadDetails(String key) {

        ref.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                model = dataSnapshot.getValue(Offer.class);

                name.setText(model.getName());
                Glide.with(getApplication()).load(model.getImage()).into(image);
                collapse.setTitle(model.getName());
                description.setText(model.getDescription());
                address.setText(model.getAddress());
                longitude = Float.valueOf(model.getLongitude());
                latitude = Float.valueOf(model.getLatitude());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // goto shop location on map
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.ratingFb)
        {
            showRationDialog();
        }
        else if(view.getId() == R.id.locationSite) {
            String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(mapIntent);
        }
    }

    private void showRationDialog() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("submit")
                .setNegativeButtonText("cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(1)
                .setTitle("Rate this Product")
                .setDescription("Please select some stars and give your feedback")
                .setStarColor(R.color.textColor)
                .setNumberOfStars(5)
                .setCommentInputEnabled(false)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(ItemDetails.this)
                .show();

    }


    private void reference() {
        name         = findViewById(R.id.name);
        image        = findViewById(R.id.item_image);
        description  = findViewById(R.id.item_description);
        address      = findViewById(R.id.address);
        location     = findViewById(R.id.locationSite);
        collapse     = findViewById(R.id.collapse);
        btnRating    = findViewById(R.id.ratingFb);
        ratingBar    = findViewById(R.id.ratingBarStars);
        value        = findViewById(R.id.ratingStatus);

        collapse.setExpandedTitleTextAppearance(R.style.expanedAppBar);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Offer");
        ratingRef = db.getReference("Rating");

    }

    @Override
    public void onPositiveButtonClicked(int value, String comment) {

        final com.example.jesus.waffarly.Model.Rating ratingModel = new com.example.jesus.waffarly.Model.Rating(model.getKey(),String.valueOf(value));

        ratingRef.push().setValue(ratingModel);
    }
    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }
}
