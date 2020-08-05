package com.example.coifsalonclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CustomRecyclerViewAdapterFrag1Services extends RecyclerView.Adapter<CustomRecyclerViewAdapterFrag1Services.ViewHolder> {

    AShop aShop;
    ShopDetails_Frag1 shopDetails_frag1;
    public CustomRecyclerViewAdapterFrag1Services(Context mContext,AShop aShop,ShopDetails_Frag1 shopDetails_frag1) {
        this.aShop=aShop;
        this.mContext = mContext;
        this.shopDetails_frag1=shopDetails_frag1;
    }

    public Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_services_recyclerview_item_frag1, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.hairCutName.setText(aShop.getServicesHairCutsNames().get(i));
        viewHolder.price.setText(aShop.getServicesHairCutsPrices().get(i) + " DA");
        viewHolder.duration.setText(aShop.getServicesHairCutsDuration().get(i) + " Min");

        if (aShop.IsBookedShop()) {
                if (aShop.getSuccessfullyBookedHaircut().equals(aShop.getServicesHairCutsNames().get(i))) {
                    viewHolder.bookButton.setText("Reserved");
                    viewHolder.bookButton.setBackgroundColor(Color.GREEN);
                } else {
                    ResetTheButtons(viewHolder);
                }
            } else {
                ResetTheButtons(viewHolder);
            }

        viewHolder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!aShop.IsBookedShop()){
                    shopDetails_frag1.shopDetailsActivity.book(aShop.getServicesHairCutsNames().get(i));
                }
               else{
                    shopDetails_frag1.shopDetailsActivity.unBook();
                }
            }
        });

    }

    void ResetTheButtons(ViewHolder viewHolder) {
        viewHolder.bookButton.setText("RESERVE");
        viewHolder.bookButton.setBackgroundColor(Color.BLUE);
    }

    @Override
    public int getItemCount() {
        return aShop.getServicesHairCutsNames().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView hairCutName;
        TextView price;
        TextView duration;

        Button bookButton;

        ConstraintLayout recyclerViewPeoplePendingItemLayout;

        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hairCutName = itemView.findViewById(R.id.hairCutNameFrag1);
            price = itemView.findViewById(R.id.priceFrag1);
            duration = itemView.findViewById(R.id.durationFrag1);
            bookButton = itemView.findViewById(R.id.book_Frag1);
            recyclerViewPeoplePendingItemLayout = itemView.findViewById(R.layout.shop_services_recyclerview_item_frag1);

        }
    }


}
