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
    private View content;
    private ImageView clothes;
    private ImageView shoes;
    private ImageView mobile;
    private ImageView laptop;
    private ImageView electronics;
    private ImageView superMarket;
    private Fragment fragment;
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
                m.userTitleName.setText(m.nvDrawer.getMenu().getItem(1).getTitle());
                break;
            case R.id.shoes:
                fragmentClass = Shoes.class;
                m.nvDrawer.getMenu().getItem(2).setChecked(true);
                m.userTitleName.setText(m.nvDrawer.getMenu().getItem(2).getTitle());
                break;
            case R.id.laptop:
                fragmentClass = Laptop.class;
                m.nvDrawer.getMenu().getItem(3).setChecked(true);
                m.userTitleName.setText(m.nvDrawer.getMenu().getItem(3).getTitle());
                break;
            case R.id.electronics:
                fragmentClass = Electronics.class;
                m.nvDrawer.getMenu().getItem(4).setChecked(true);
                m.userTitleName.setText(m.nvDrawer.getMenu().getItem(4).getTitle());
                break;
            case R.id.mobile:
                fragmentClass = Mobile.class;
                m.nvDrawer.getMenu().getItem(5).setChecked(true);
                m.userTitleName.setText(m.nvDrawer.getMenu().getItem(5).getTitle());
                break;
            case R.id.superMarket:
                fragmentClass = SuperMarket.class;
                m.nvDrawer.getMenu().getItem(6).setChecked(true);
                m.userTitleName.setText(m.nvDrawer.getMenu().getItem(6).getTitle());
                break;
        }
        showDesirePage(fragmentClass);
    }

    // to show the desire fragment
    private void showDesirePage(Class fragmentClass) {
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

        clothes      = content.findViewById(R.id.clothes_btn);
        shoes        = content.findViewById(R.id.shoes);
        superMarket  = content.findViewById(R.id.superMarket);
        mobile       = content.findViewById(R.id.mobile);
        laptop       = content.findViewById(R.id.laptop);
        electronics  = content.findViewById(R.id.electronics);
    }
}
