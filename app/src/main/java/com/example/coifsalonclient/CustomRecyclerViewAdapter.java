package com.example.coifsalonclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

    ArrayList<String> ShopNamesList =new ArrayList<>();
    ArrayList<String> ShopAddresses =new ArrayList<>();
    ArrayList<Bitmap> ShopsImages =new ArrayList<>();
    ArrayList<ArrayList<String>> ShopsImagesLinks =new ArrayList<>();
    String  successfullyBookedShop;
    String successfullyBookedHaircut;
    Context mContext;


    public CustomRecyclerViewAdapter(Context context, ArrayList<String> ShopNamesList, ArrayList<String> ShopAddresses, ArrayList<Bitmap> ShopsImages, ArrayList<ArrayList<String>> ShopsImagesLinks, String successfullyBookedShop, String successfullyBookedHaircut) {

        this.ShopNamesList = ShopNamesList;
        this.ShopAddresses = ShopAddresses;
        this.ShopsImages = ShopsImages;
        this.ShopsImagesLinks = ShopsImagesLinks;
        this.successfullyBookedShop = successfullyBookedShop;
        this.successfullyBookedHaircut = successfullyBookedHaircut;
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

        if(i< ShopNamesList.size()) {
            viewHolder.ShopNameTextView.setText(ShopNamesList.get(i));

        }
        if(i< ShopAddresses.size()) {
            viewHolder.ShopAddressTextView.setText(ShopAddresses.get(i));

}
       if(i< ShopsImages.size()) {
           viewHolder.ShopMainImageImageView.setImageBitmap(ShopsImages.get(i));

       }

       viewHolder.radioGroup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent goToShopDetailsActivity = new Intent(mContext, ShopDetailsActivity.class);
               goToShopDetailsActivity.putExtra("ShopName", ShopNamesList.get(i));
               goToShopDetailsActivity.putExtra("successfullyBookedShop", successfullyBookedShop);
               goToShopDetailsActivity.putExtra("successfullyBookedHaircut", successfullyBookedHaircut);
               if(i< ShopsImagesLinks.size()){
                   goToShopDetailsActivity.putExtra("ImagesLinks", ShopsImagesLinks.get(i));
               }

               mContext.startActivity(goToShopDetailsActivity);
           }
       });
    }


    @Override
    public int getItemCount() {
        return ShopNamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ShopMainImageImageView;
        TextView ShopNameTextView;
        TextView ShopAddressTextView;
        RadioGroup radioGroup;
        ConstraintLayout recyclerViewPeoplePendingItemLayout;

        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ShopMainImageImageView =itemView.findViewById(R.id.storeMainImage);
            ShopNameTextView =itemView.findViewById(R.id.storeName);
            ShopAddressTextView =itemView.findViewById(R.id.storeAddress);
            radioGroup=itemView.findViewById(R.id.radioGroup);
            recyclerViewPeoplePendingItemLayout=itemView.findViewById(R.layout.search_result_recyclerview_item);
        }
    }
}
