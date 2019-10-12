package com.example.coifsalonclient;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.gms.maps.model.LatLng;

public class ShopDetails_Frag3 extends Fragment {
   static  CustomRecyclerViewAdapterFrag3Reviews customRecyclerViewAdapterFrag3Reviews;
   static  RecyclerView recyclerView;
    public static Context mContext;
    Button AddReviewButton_Frag3;
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
        AddReviewButton_Frag3=view.findViewById(R.id.AddReviewButton_Frag3);
        AddReviewButton_Frag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder AlertDialogBuilder=new AlertDialog.Builder(mContext);
                LayoutInflater layoutInflater=getLayoutInflater();
                final View DialogView=layoutInflater.inflate(R.layout.add_review_alertdialog_view_frag3,null);
                AlertDialogBuilder.setView(DialogView);

               AlertDialogBuilder.setPositiveButton(R.string.AddReviewSubmitPositiveAlarDialog_Frag3, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       EditText ReviewerNameEditText=DialogView.findViewById(R.id.reviewerNameToAdd);
                       EditText ReviewerCommentEditText=DialogView.findViewById(R.id.reviewerCommentToAdd);
                       RatingBar RatingBar=DialogView.findViewById(R.id.ratingBarToAdd);
                       ((ShopDetailsActivity)getActivity()).AddReview(ReviewerNameEditText.getText().toString(), ReviewerCommentEditText.getText().toString(), RatingBar.getRating());
                   }
               });
               AlertDialogBuilder.setNegativeButton(R.string.AddReviewSubmitNegativeAlarDialog_Frag3, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               });
               AlertDialogBuilder.create().show();
            }
        });
        return view;
    }

}
