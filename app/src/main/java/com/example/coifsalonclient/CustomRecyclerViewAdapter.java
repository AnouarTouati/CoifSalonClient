package com.example.coifsalonclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

    ArrayList<String> StoreNamesList=new ArrayList<>();
    Context mContext;
    public CustomRecyclerViewAdapter(Context context, ArrayList<String> storeNamesList) {

        this.StoreNamesList=storeNamesList;
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_recyclerview_item, viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return  viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.storeNameTextView.setText(StoreNamesList.get(i));
        viewHolder.storeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToShopDetailsActivity=new Intent(mContext,ShopDetailsActivity.class);
                goToShopDetailsActivity.putExtra("ShopName", StoreNamesList.get(i));
                mContext.startActivity(goToShopDetailsActivity);
            }
        });


       viewHolder.storeAddressTextView.setText(StoreNamesList.get(i));
       viewHolder.storeAddressTextView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent goToShopDetailsActivity=new Intent(mContext,ShopDetailsActivity.class);
               goToShopDetailsActivity.putExtra("ShopIndexInRecycler", i);
               mContext.startActivity(goToShopDetailsActivity);
           }
       });

       viewHolder.storeMainImageImageView.setImageResource(R.drawable.picturetouse);
       viewHolder.storeMainImageImageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent goToShopDetailsActivity=new Intent(mContext,ShopDetailsActivity.class);
               goToShopDetailsActivity.putExtra("ShopIndexInRecycler", i);
               mContext.startActivity(goToShopDetailsActivity);
           }
       });

    }


    @Override
    public int getItemCount() {
        return StoreNamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView storeMainImageImageView;
        TextView storeNameTextView;
        TextView storeAddressTextView;
        ConstraintLayout recyclerViewPeoplePendingItemLayout;

        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeMainImageImageView=itemView.findViewById(R.id.storeMainImage);
            storeNameTextView=itemView.findViewById(R.id.storeName);
            storeAddressTextView=itemView.findViewById(R.id.storeAddress);
            recyclerViewPeoplePendingItemLayout=itemView.findViewById(R.layout.search_result_recyclerview_item);
        }
    }
}
