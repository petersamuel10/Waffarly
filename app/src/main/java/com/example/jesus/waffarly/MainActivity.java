package com.example.jesus.waffarly;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView main_profile;
    public static TextView user_title_name;
    View header;
    CircleImageView nv_profile;
    TextView nv_user_name;
    ImageView clothes;
    ImageView shoes;
    ImageView laptop;
    ImageView electronics;
    ImageView mobile;
    ImageView supermarket;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageRef;
    public String user_id;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    public static NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    // Create a new fragment and specify the fragment to show based on nav item clicked
    Fragment fragment = null;
    Class fragmentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //references
        reference();

        //check login
       ifLogin();
        FirebaseMessaging.getInstance().subscribeToTopic("notifications");
        // Set a Toolbar to replace the ActionBar.
        setSupportActionBar(toolbar);

        // Setup drawer view
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();
        mDrawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // to open app with Home Fragment
        chooseFragment(Home.class);

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView nvDrawer) {

        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                selectDrawerItem(item);
                return false;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.home:
                fragmentClass =Home.class;
                break;

            case R.id.clothes:
                fragmentClass =Clothes.class;
                break;
            case R.id.shoes:
                fragmentClass =Shoes.class;
                break;
            case R.id.laptop:
                fragmentClass =Laptop.class;
                break;
            case R.id.electronics:
                fragmentClass =Electronics.class;
                break;
            case R.id.mobile:
                fragmentClass =Mobile.class;
                break;
            case R.id.superMarket:
                fragmentClass =SuperMarket.class;
                break;
            case R.id.add:
                fragmentClass =Add_Offer.class;
                break;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(this.getApplication(),Login.class));
                finish();
                break;
            default:
                    fragmentClass =Home.class;

        }

        chooseFragment(fragmentClass);
        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title to all fragment expect Home show the user name
        user_title_name.setText(item.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private void chooseFragment(Class fragmentClass) {

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_view, fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reference() {

        //for toolbar
        main_profile = (CircleImageView)findViewById(R.id.main_profile);
        user_title_name = (TextView)findViewById(R.id.user_title_name);

        clothes =(ImageView)findViewById(R.id.clothes_btn);
        shoes =(ImageView)findViewById(R.id.shoes);
        laptop =(ImageView)findViewById(R.id.laptop);
        electronics =(ImageView)findViewById(R.id.electronics);
        mobile =(ImageView)findViewById(R.id.mobile);
        supermarket =(ImageView)findViewById(R.id.superMarket);

        mDrawer=(DrawerLayout)findViewById(R.id.drawer);

        nvDrawer=(NavigationView)findViewById(R.id.nvView);
        toolbar= (Toolbar) findViewById(R.id.toolbar);

        header = nvDrawer.inflateHeaderView(R.layout.nv_header);

    }
    private void ifLogin() {

        mAuth=FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        //login with the recent login user
        FirebaseUser current_User = mAuth.getCurrentUser();
        //check if there is save login or not
        if(current_User == null) {
            startActivity(new Intent(this, Login.class));
            finish();
        }
        else
            loadNameAndProfile();

    }
    private void loadNameAndProfile() {

        user_id = mAuth.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference().child("images/"+user_id+".jpg");

        // for navigation header drawer
           nv_profile =(CircleImageView) header.findViewById(R.id.nv_profile_header);
           nv_user_name = (TextView) header.findViewById(R.id.nv_user_name_header);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               user_title_name.setText(dataSnapshot.child("Users").child(user_id).child("name").getValue(String.class));
               nv_user_name.setText(user_title_name.getText());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //load profile picture
          Glide.with(this).using(new FirebaseImageLoader()).load(storageRef).into(main_profile);
          Glide.with(this).using(new FirebaseImageLoader()).load(storageRef).into(nv_profile);
    }
}
