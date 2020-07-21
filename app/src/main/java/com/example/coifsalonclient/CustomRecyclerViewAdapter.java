package com.example.coifsalonclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

    ArrayList<String> shopNamesList =new ArrayList<>();
    ArrayList<String> shopAddresses =new ArrayList<>();
    ArrayList<Bitmap> shopsImages =new ArrayList<>();
    ArrayList<ArrayList<String>> shopsImagesLinks =new ArrayList<>();
    String  successfullyBookedShop;
    String successfullyBookedHaircut;
    Context mContext;


    public CustomRecyclerViewAdapter(Context context, ArrayList<String> ShopNamesList, ArrayList<String> ShopAddresses, ArrayList<Bitmap> ShopsImages, ArrayList<ArrayList<String>> ShopsImagesLinks, String successfullyBookedShop, String successfullyBookedHaircut) {

        this.shopNamesList = ShopNamesList;
        this.shopAddresses = ShopAddresses;
        this.shopsImages = ShopsImages;
        this.shopsImagesLinks = ShopsImagesLinks;
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

        if(i< shopNamesList.size()) {
            viewHolder.shopNameTextView.setText(shopNamesList.get(i));

        }
        if(i< shopAddresses.size()) {
            viewHolder.shopAddressTextView.setText(shopAddresses.get(i));

}
       if(i< shopsImages.size()) {
           viewHolder.shopMainImageImageView.setImageBitmap(shopsImages.get(i));

       }

       viewHolder.radioGroup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent goToShopDetailsActivity = new Intent(mContext, ShopDetailsActivity.class);
               goToShopDetailsActivity.putExtra("ShopName", shopNamesList.get(i));
               goToShopDetailsActivity.putExtra("SuccessfullyBookedShop", successfullyBookedShop);
               goToShopDetailsActivity.putExtra("SuccessfullyBookedHaircut", successfullyBookedHaircut);
               if(i< shopsImagesLinks.size()){
                   goToShopDetailsActivity.putExtra("ImagesLinks", shopsImagesLinks.get(i));
               }

               mContext.startActivity(goToShopDetailsActivity);
           }
       });
    }


    @Override
    public int getItemCount() {
        return shopNamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView shopMainImageImageView;
        TextView shopNameTextView;
        TextView shopAddressTextView;
        RadioGroup radioGroup;
        ConstraintLayout recyclerViewPeoplePendingItemLayout;

        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopMainImageImageView =itemView.findViewById(R.id.storeMainImage);
            shopNameTextView =itemView.findViewById(R.id.storeName);
            shopAddressTextView =itemView.findViewById(R.id.storeAddress);
            radioGroup=itemView.findViewById(R.id.radioGroup);
            recyclerViewPeoplePendingItemLayout=itemView.findViewById(R.layout.search_result_recyclerview_item);
        }
    }
}
