package com.example.coifsalonclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

    ArrayList<String> StoreNamesList=new ArrayList<>();
    ArrayList<String> StoresAddresses =new ArrayList<>();
    ArrayList<Bitmap> StoresImages=new ArrayList<>();
    Context mContext;
    public CustomRecyclerViewAdapter(Context context, ArrayList<String> storeNamesList,ArrayList<String> StoresAddresses,ArrayList<Bitmap> StoresImages) {

        this.StoreNamesList=storeNamesList;
        this.StoresAddresses=StoresAddresses;
        this.StoresImages=StoresImages;
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

        if(i<StoreNamesList.size()) {
            viewHolder.storeNameTextView.setText(StoreNamesList.get(i));

        }
        if(i<StoresAddresses.size()) {
            viewHolder.storeAddressTextView.setText(StoresAddresses.get(i));

}
       if(i<StoresImages.size()) {
           viewHolder.storeMainImageImageView.setImageBitmap(StoresImages.get(i));

       }

       viewHolder.radioGroup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent goToShopDetailsActivity = new Intent(mContext, ShopDetailsActivity.class);
               goToShopDetailsActivity.putExtra("ShopName", StoreNamesList.get(i));

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
        RadioGroup radioGroup;
        ConstraintLayout recyclerViewPeoplePendingItemLayout;

        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeMainImageImageView=itemView.findViewById(R.id.storeMainImage);
            storeNameTextView=itemView.findViewById(R.id.storeName);
            storeAddressTextView=itemView.findViewById(R.id.storeAddress);
            radioGroup=itemView.findViewById(R.id.radioGroup);
            recyclerViewPeoplePendingItemLayout=itemView.findViewById(R.layout.search_result_recyclerview_item);
        }
    }
}
