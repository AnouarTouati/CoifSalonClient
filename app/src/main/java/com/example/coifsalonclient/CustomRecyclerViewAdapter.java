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


    ArrayList<AShop> aShopsList=new ArrayList<>();
    ArrayList<Bitmap> shopsImages =new ArrayList<>();
    MainActivity mainActivity;
    Context mContext;


    public CustomRecyclerViewAdapter(Context context, MainActivity mainActivity, ArrayList<AShop> aShopsList, ArrayList<Bitmap> ShopsImages) {

        this.aShopsList=aShopsList;
        this.shopsImages = ShopsImages;
        this.mainActivity=mainActivity;
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

        if(i < aShopsList.size()) {
            viewHolder.shopNameTextView.setText(aShopsList.get(i).getShopName());
        }
        if(i < aShopsList.size()) {
            viewHolder.shopAddressTextView.setText(aShopsList.get(i).getSelectedCommune()+" "+aShopsList.get(i).getSelectedState());
        }
       if(i < shopsImages.size()) {
           viewHolder.shopMainImageImageView.setImageBitmap(shopsImages.get(i));
       }

       viewHolder.radioGroup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent goToShopDetailsActivity = new Intent(mContext, ShopDetailsActivity.class);
               if(mainActivity.successfullyBookedShop!=null){
                   if(aShopsList.get(i).getShopUid().equals(mainActivity.successfullyBookedShop.getShopUid())){
                       aShopsList.get(i).setBookedShop(true);
                       aShopsList.get(i).setSuccessfullyBookedHaircut(mainActivity.successfullyBookedShop.getSuccessfullyBookedHaircut());
                   }
               }
               goToShopDetailsActivity.putExtra("aShop",aShopsList.get(i));
               mContext.startActivity(goToShopDetailsActivity);
           }
       });
    }


    @Override
    public int getItemCount() {
        return aShopsList.size();
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
