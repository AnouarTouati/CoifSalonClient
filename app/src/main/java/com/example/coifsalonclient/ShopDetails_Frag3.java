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

public class ShopDetails_Frag3 extends Fragment {
   static  CustomRecyclerViewAdapterFrag3Reviews customRecyclerViewAdapterFrag3Reviews;
   static  RecyclerView recyclerView;
    public static Context mContext;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getContext();
        view=inflater.inflate(R.layout.shopdetails_frag3, container, false);
        recyclerView=view.findViewById(R.id.recyclerView_Frag3);
        customRecyclerViewAdapterFrag3Reviews=new CustomRecyclerViewAdapterFrag3Reviews(ShopDetailsActivity.reviewersNames, ShopDetailsActivity.reviewersComments, ShopDetailsActivity.reviewersCommentDate, ShopDetailsActivity.reviewersGivenStars, getContext());
        recyclerView.setAdapter(customRecyclerViewAdapterFrag3Reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

}
