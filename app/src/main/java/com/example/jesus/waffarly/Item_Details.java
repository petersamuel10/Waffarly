package com.example.jesus.waffarly;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Item_Details extends AppCompatActivity {

    private ImageView image;
    private TextView name;
    private TextView description;
    private TextView address;

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
    }

    private void reference() {
        image        = (ImageView)findViewById(R.id.item_image);
        name         = (TextView)findViewById(R.id.shop_name);
        description  = (TextView)findViewById(R.id.item_description);
        address      = (TextView)findViewById(R.id.address);
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
}
