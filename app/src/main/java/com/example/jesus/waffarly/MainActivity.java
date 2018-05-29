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
    private CircleImageView mainProfile;
    public static TextView userTitleName;
    private View header;
    private CircleImageView nvProfile;
    private TextView nvUserName;
    private ImageView clothes;
    private ImageView shoes;
    private ImageView laptop;
    private ImageView electronics;
    private ImageView mobile;
    private ImageView supermarket;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageRef;
    public String user_id;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    public static NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    // Create a new fragment and specify the fragment to show based on nav item clicked
    private Fragment fragment = null;
    private Class fragmentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //references
        reference();

        //check login
       checkRegister();
       // set topic to can receive notification
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
                fragmentClass =AddOffer.class;
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
        userTitleName.setText(item.getTitle());
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
        mainProfile =findViewById(R.id.main_profile);
        userTitleName = findViewById(R.id.user_title_name);

        clothes =findViewById(R.id.clothes_btn);
        shoes =findViewById(R.id.shoes);
        laptop =findViewById(R.id.laptop);
        electronics =findViewById(R.id.electronics);
        mobile =findViewById(R.id.mobile);
        supermarket =findViewById(R.id.superMarket);

        mDrawer=findViewById(R.id.drawer);

        nvDrawer=findViewById(R.id.nvView);
        toolbar= findViewById(R.id.toolbar);

        header = nvDrawer.inflateHeaderView(R.layout.nv_header);

    }
    private void checkRegister() {

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
           nvProfile =(CircleImageView) header.findViewById(R.id.nv_profile_header);
           nvUserName = (TextView) header.findViewById(R.id.nv_user_name_header);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               userTitleName.setText(dataSnapshot.child("Users").child(user_id).child("name").getValue(String.class));
               nvUserName.setText(userTitleName.getText());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //load profile picture
          Glide.with(this).using(new FirebaseImageLoader()).load(storageRef).into(mainProfile);
          Glide.with(this).using(new FirebaseImageLoader()).load(storageRef).into(nvProfile);
    }
}
