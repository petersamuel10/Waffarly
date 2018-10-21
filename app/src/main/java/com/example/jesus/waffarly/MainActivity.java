package com.example.jesus.waffarly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jesus.waffarly.Categories.Clothes;
import com.example.jesus.waffarly.Categories.Electronics;
import com.example.jesus.waffarly.Categories.Laptop;
import com.example.jesus.waffarly.Categories.Mobile;
import com.example.jesus.waffarly.Categories.Shoes;
import com.example.jesus.waffarly.Categories.SuperMarket;
import com.example.jesus.waffarly.Common.Common;
import com.example.jesus.waffarly.Model.User;
import com.example.jesus.waffarly.Service.ListenNewOffer;
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
        if(Common.isConnectToTheInternet(getBaseContext())) {
            //check login
            checkRegister();
            // set topic to can receive notification
            FirebaseMessaging.getInstance().subscribeToTopic("notifications");
        }else
            Toast.makeText(getBaseContext(), "Please Check Ihe Internet Connection !!!", Toast.LENGTH_SHORT).show();

        // Set a Toolbar to replace the ActionBar.
        setSupportActionBar(toolbar);

        //register to notification
        Intent notification = new Intent(this, ListenNewOffer.class);
        startService(notification);

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
                    break;

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
        if (item.getItemId()== android.R.id.home ) {
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reference() {

        //for toolbar
        mainProfile =findViewById(R.id.main_profile);
        userTitleName = findViewById(R.id.user_title_name);
        mDrawer=findViewById(R.id.drawer);
        nvDrawer=findViewById(R.id.nvView);
        toolbar= findViewById(R.id.toolbar);
        header = nvDrawer.inflateHeaderView(R.layout.nv_header);

    }
    private void checkRegister() {

        mAuth=FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

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


        storageRef = FirebaseStorage.getInstance().getReference().child("images/"+user_id);

        // for navigation header drawer
           nvProfile = header.findViewById(R.id.nv_profile_header);
           nvUserName = header.findViewById(R.id.nv_user_name_header);

            databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user_id).exists())
                {
                    Log.e("id",user_id);
                    User userInf = dataSnapshot.child(user_id).getValue(User.class);
                    userTitleName.setText(userInf.getName());
                    nvUserName.setText(userInf.getName());
                    //load profile picture
                    Glide.with(getBaseContext()).load(userInf.getProfileUri()).into(mainProfile);
                    Glide.with(getBaseContext()).load(userInf.getProfileUri()).into(nvProfile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
