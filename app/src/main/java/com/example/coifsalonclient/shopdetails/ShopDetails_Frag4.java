package com.example.coifsalonclient.shopdetails;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coifsalonclient.R;

public class ShopDetails_Frag4 extends Fragment {
    static RecyclerView recyclerViewFrag4Portfolio;
    CustomRecyclerViewAdapterFrag4Portfolio customRecyclerViewAdapterFrag4Portfolio;

    public  Context mContext;
    private final ShopDetailsActivity shopDetailsActivity;
    View view;
    public ShopDetails_Frag4(ShopDetailsActivity shopDetailsActivity){
        this.shopDetailsActivity=shopDetailsActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mContext=getContext();
       view=inflater.inflate(R.layout.shopdetails_frag4, container,false);
       recyclerViewFrag4Portfolio =view.findViewById(R.id.recyclerView_Fra4);
        customRecyclerViewAdapterFrag4Portfolio =new CustomRecyclerViewAdapterFrag4Portfolio(shopDetailsActivity.portfolioPhotos,mContext);
        recyclerViewFrag4Portfolio.setAdapter(customRecyclerViewAdapterFrag4Portfolio);
        recyclerViewFrag4Portfolio.setLayoutManager(new LinearLayoutManager(mContext));

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        receivedNewImagesNotifyRecyclerView();

    }

    public void receivedNewImagesNotifyRecyclerView(){

        if(recyclerViewFrag4Portfolio!=null){
            customRecyclerViewAdapterFrag4Portfolio =new CustomRecyclerViewAdapterFrag4Portfolio(shopDetailsActivity.portfolioPhotos,mContext);
            recyclerViewFrag4Portfolio.swapAdapter(customRecyclerViewAdapterFrag4Portfolio, true);
        }
    }
}
