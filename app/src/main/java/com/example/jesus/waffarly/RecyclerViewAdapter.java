package com.example.jesus.waffarly;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
        TextView name = holder.itemName;
        name.setText(item_data.getName());
        TextView summary = holder.itemSummary;
        summary.setText(item_data.getSummary());

        // send item details and open it when click on item recycler view
         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent item_details = new Intent(mContext,ItemDetails.class);
                item_details.putExtra("item",
                        new String[]{item_data.getImage(),item_data.getName(),item_data.getDescription()
                                ,item_data.getAddress(),item_data.getLongitude(),item_data.getLatitude()});
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

        public View itemView;
        public TextView itemName;
        public TextView itemSummary;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView = itemView;
            itemName = itemView.findViewById(R.id.item_name);
            itemSummary = itemView.findViewById(R.id.item_summary);
        }
    }

}
