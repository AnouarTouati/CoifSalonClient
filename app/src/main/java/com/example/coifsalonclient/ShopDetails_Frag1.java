package com.example.coifsalonclient;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShopDetails_Frag1 extends Fragment {
    View view;
   static  RecyclerView recyclerView;
    static CustomRecyclerViewAdapterFrag1Services  customRecyclerViewAdapterFrag1Services;
   public static Context mContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.shopdetails_frag1, container,false);

        mContext=getContext();
        recyclerView=view.findViewById(R.id.recyclerView_Frag1);
        customRecyclerViewAdapterFrag1Services=new CustomRecyclerViewAdapterFrag1Services(ShopDetailsActivity.successfullyBookedHaircut,ShopDetailsActivity.successfullyBookedShop,ShopDetailsActivity.servicesHairCutsNames,ShopDetailsActivity.servicesHairCutsDuration,ShopDetailsActivity.servicesHairCutsPrices,mContext);
        recyclerView.setAdapter(customRecyclerViewAdapterFrag1Services);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        return view;
    }
    public static void callbook(String ServiceHairCutToReseve){
   ShopDetailsActivity.book(ServiceHairCutToReseve);
    }
    public static void BookWasSuccessfulNorifyRecyclerViewAdapter(){

        customRecyclerViewAdapterFrag1Services=new CustomRecyclerViewAdapterFrag1Services(ShopDetailsActivity.successfullyBookedHaircut,ShopDetailsActivity.successfullyBookedShop,ShopDetailsActivity.servicesHairCutsNames,ShopDetailsActivity.servicesHairCutsDuration,ShopDetailsActivity.servicesHairCutsPrices,mContext);
    recyclerView.swapAdapter(customRecyclerViewAdapterFrag1Services,true);

    }
}
