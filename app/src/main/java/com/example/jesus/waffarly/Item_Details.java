package com.example.jesus.waffarly;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Item_Details extends AppCompatActivity implements View.OnClickListener{

    private ImageView image;
    private TextView name;
    private TextView description;
    private TextView address;
    private ImageView location;
    Float longitude = 0.0f;
    Float latitude = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //reference
        reference();
        location.setOnClickListener(this);
    }

    private void reference() {
        image        = (ImageView)findViewById(R.id.item_image);
        name         = (TextView)findViewById(R.id.shop_name);
        description  = (TextView)findViewById(R.id.item_description);
        address      = (TextView)findViewById(R.id.address);
        location     = (ImageView) findViewById(R.id.locationSite);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String [] item = getIntent().getStringArrayExtra("item");
        //image.setImageURI(Uri.parse(item[0]));
        name.setText(item[1]);
        description.setText(item[2]);
        address.setText(item[3]);
        Glide.with(getApplication()).load(item[0]).into(image);

        try {
            longitude = Float.valueOf(item[4]);
            latitude = Float.valueOf(item[5]);
        }catch (Exception e)
        {
            Log.e("errorrr",e.getMessage().toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        String geoUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(mapIntent);

    }
}
