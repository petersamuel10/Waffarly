package com.example.jesus.waffarly;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Home extends Fragment implements View.OnClickListener{
    View content;
    ImageView clothes;
    ImageView shoes;
    ImageView mobile;
    ImageView laptop;
    ImageView electronics;
    ImageView superMarket;
    AppCompatActivity activity;
    Fragment fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        content = inflater.inflate(R.layout.home, container, false);
        //reference
        Reference();
        clothes.setOnClickListener(this);
        shoes.setOnClickListener(this);
        electronics.setOnClickListener(this);
        mobile.setOnClickListener(this);
        laptop.setOnClickListener(this);
        superMarket.setOnClickListener(this);

        return content;
    }
    @Override
    public void onClick(View view) {


        MainActivity m = new MainActivity();
       Class fragmentClass = null;
        int id = view.getId();

        switch (id)
        {
            case R.id.clothes_btn:
                fragmentClass = Clothes.class;
                m.nvDrawer.getMenu().getItem(1).setChecked(true);
                m.user_title_name.setText(m.nvDrawer.getMenu().getItem(1).getTitle());
                break;
            case R.id.shoes:
                fragmentClass = Shoes.class;
                m.nvDrawer.getMenu().getItem(2).setChecked(true);
                m.user_title_name.setText(m.nvDrawer.getMenu().getItem(2).getTitle());
                break;
            case R.id.laptop:
                fragmentClass = Laptop.class;
                m.nvDrawer.getMenu().getItem(3).setChecked(true);
                m.user_title_name.setText(m.nvDrawer.getMenu().getItem(3).getTitle());
                break;
            case R.id.electronics:
                fragmentClass = Electronics.class;
                m.nvDrawer.getMenu().getItem(4).setChecked(true);
                m.user_title_name.setText(m.nvDrawer.getMenu().getItem(4).getTitle());
                break;
            case R.id.mobile:
                fragmentClass = Mobile.class;
                m.nvDrawer.getMenu().getItem(5).setChecked(true);
                m.user_title_name.setText(m.nvDrawer.getMenu().getItem(5).getTitle());
                break;
            case R.id.superMarket:
                fragmentClass = SuperMarket.class;
                m.nvDrawer.getMenu().getItem(6).setChecked(true);
                m.user_title_name.setText(m.nvDrawer.getMenu().getItem(6).getTitle());
                break;
        }
        chooseImageView(fragmentClass);
    }

    private void chooseImageView(Class fragmentClass) {
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_view, fragment);
        transaction.commit();
    }


    private void Reference() {

        clothes      = (ImageView)content.findViewById(R.id.clothes_btn);
        shoes        = (ImageView)content.findViewById(R.id.shoes);
        superMarket  = (ImageView)content.findViewById(R.id.superMarket);
        mobile       = (ImageView)content.findViewById(R.id.mobile);
        laptop       = (ImageView)content.findViewById(R.id.laptop);
        electronics  = (ImageView)content.findViewById(R.id.electronics);
    }
}
