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

public class ShopDetails_Frag4 extends Fragment {
    static RecyclerView recyclerViewFrag4Portfolio;
    static CustomRecyclerViewAdapterFrag4Portfolio customRecyclerViewAdapterFrag4Portfolio;

    public static Context mContext;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mContext=getContext();
       view=inflater.inflate(R.layout.shopdetails_frag4, container,false);
       recyclerViewFrag4Portfolio =view.findViewById(R.id.recyclerView_Fra4);
        customRecyclerViewAdapterFrag4Portfolio =new CustomRecyclerViewAdapterFrag4Portfolio(ShopDetailsActivity.portfolioImages,mContext);
        recyclerViewFrag4Portfolio.setAdapter(customRecyclerViewAdapterFrag4Portfolio);
        recyclerViewFrag4Portfolio.setLayoutManager(new LinearLayoutManager(mContext));

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        receivedNewImagesNotifyRecyclerView();

    }

    public static void receivedNewImagesNotifyRecyclerView(){
       customRecyclerViewAdapterFrag4Portfolio =new CustomRecyclerViewAdapterFrag4Portfolio(ShopDetailsActivity.portfolioImages,mContext);
       recyclerViewFrag4Portfolio.swapAdapter(customRecyclerViewAdapterFrag4Portfolio, true);
    }
}
