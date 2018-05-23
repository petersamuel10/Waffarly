package com.example.jesus.waffarly;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jesus on 5/12/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<RecyclerModel> myList;
    private Context mContext;

    public RecyclerViewAdapter (Context context,List<RecyclerModel>list)
    {
        this.myList=list;
        this.mContext=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate the custom layout
        View itemView = inflater.inflate(R.layout.item,parent,false);
        // Return a new holder instance
        MyViewHolder viewHolder = new MyViewHolder(itemView);

        return viewHolder;
    }


    // Involves populating data into the item through holder
    // draw views and bind data on it
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final RecyclerModel item_data = myList.get(position);
        // Set item views based on your views and data model
        TextView name = holder.item_name;
        name.setText(item_data.getName());
        TextView summary = holder.item_summary;
        summary.setText(item_data.getSummary());

        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent item_details = new Intent(mContext,Item_Details.class);
                item_details.putExtra("item",
                        new String[]{item_data.getImage(),item_data.getName(),item_data.getDescription(),item_data.getAddress()});
                mContext.startActivity(item_details);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        public View item_view;
        public TextView item_name;
        public TextView item_summary;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_view = itemView;
            item_name = (TextView)item_view.findViewById(R.id.item_name);
            item_summary = (TextView)item_view.findViewById(R.id.item_summary);
        }
    }

}
