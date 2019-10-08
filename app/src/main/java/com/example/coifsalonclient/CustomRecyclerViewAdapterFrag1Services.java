package com.example.coifsalonclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomRecyclerViewAdapterFrag1Services extends RecyclerView.Adapter<CustomRecyclerViewAdapterFrag1Services.ViewHolder> {


public ArrayList<String> servicesHairCuts=new ArrayList<>();
public ArrayList<String> servicesHaircutDurations=new ArrayList<>();
public ArrayList<String> servicesHairCutPrice=new ArrayList<>();
public String successfullyBookedHaircut =null;
public String successfullyBookedShop =null;

    public CustomRecyclerViewAdapterFrag1Services(String successfullyBookedHaircut, String successfullyBookedShop, ArrayList<String> servicesHairCuts, ArrayList<String> servicesHaircutDurations, ArrayList<String> servicesHairCutPrice, Context mContext) {
        this.successfullyBookedHaircut = successfullyBookedHaircut;
        this.successfullyBookedShop = successfullyBookedShop;
        this.servicesHairCuts = servicesHairCuts;
        this.servicesHaircutDurations = servicesHaircutDurations;
        this.servicesHairCutPrice = servicesHairCutPrice;
        this.mContext = mContext;
    }

    public Context mContext;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_services_recyclerview_item_frag1, viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return  viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.hairCutName.setText(servicesHairCuts.get(i));
      viewHolder.price.setText(servicesHairCutPrice.get(i)+" DA");
      viewHolder.duration.setText(servicesHaircutDurations.get(i)+" Min");
      if(successfullyBookedHaircut !=null && successfullyBookedShop !=null){
          if(successfullyBookedShop.equals(ShopDetailsActivity.ShopNameFromRecyclerView)){
              if(successfullyBookedHaircut.equals(servicesHairCuts.get(i))){
                  viewHolder.bookButton.setText("Resrved");
                  viewHolder.bookButton.setBackgroundColor(Color.GREEN);
              }else{
                  ResetTheButtons(viewHolder);
              }
          }else{
              ResetTheButtons(viewHolder);
          }

      }else{
          ResetTheButtons(viewHolder);
      }

      viewHolder.bookButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
          ShopDetails_Frag1.callbook(servicesHairCuts.get(i));
          }
      });

    }
    void ResetTheButtons(ViewHolder viewHolder){
        viewHolder.bookButton.setText("RESERVE");
        viewHolder.bookButton.setBackgroundColor(Color.BLUE);
    }

    @Override
    public int getItemCount() {
        return servicesHairCuts.size();
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
            hairCutName=itemView.findViewById(R.id.hairCutNameFrag1);
            price=itemView.findViewById(R.id.priceFrag1);
            duration=itemView.findViewById(R.id.durationFrag1);
            bookButton=itemView.findViewById(R.id.book_Frag1);
            recyclerViewPeoplePendingItemLayout=itemView.findViewById(R.layout.store_services_recyclerview_item_frag1);

        }
    }


}
