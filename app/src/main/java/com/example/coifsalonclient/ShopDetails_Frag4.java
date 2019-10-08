package com.example.coifsalonclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShopDetails_Frag4 extends Fragment {
    static RecyclerView RecyclerViewFrag4Portfolio;
    static CustomRecyclerViewAdapterFrag4Portfolio CustomRecyclerViewAdapterFrag4Portfolio;

    public static Context mContext;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mContext=getContext();
       view=inflater.inflate(R.layout.shopdetails_frag4, container,false);
       RecyclerViewFrag4Portfolio=view.findViewById(R.id.recyclerView_Fra4);
        CustomRecyclerViewAdapterFrag4Portfolio=new CustomRecyclerViewAdapterFrag4Portfolio(ShopDetailsActivity.PortfolioImages,mContext);
        RecyclerViewFrag4Portfolio.setAdapter(CustomRecyclerViewAdapterFrag4Portfolio);
        RecyclerViewFrag4Portfolio.setLayoutManager(new LinearLayoutManager(mContext));

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ReceivedNewImagesNotifyRecyclerView();

    }

    public static void ReceivedNewImagesNotifyRecyclerView(){
       CustomRecyclerViewAdapterFrag4Portfolio=new CustomRecyclerViewAdapterFrag4Portfolio(ShopDetailsActivity.PortfolioImages,mContext);
       RecyclerViewFrag4Portfolio.swapAdapter(CustomRecyclerViewAdapterFrag4Portfolio, true);
    }
}
