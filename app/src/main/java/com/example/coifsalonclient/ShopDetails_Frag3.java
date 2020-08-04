package com.example.coifsalonclient;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class ShopDetails_Frag3 extends Fragment {
   static  CustomRecyclerViewAdapterFrag3Reviews customRecyclerViewAdapterFrag3Reviews;
   static  RecyclerView recyclerView;
    public static Context mContext;
    Button addReviewButtonFrag3;
    ShopDetailsActivity shopDetailsActivity;
    View view;

    public ShopDetails_Frag3(ShopDetailsActivity shopDetailsActivity){
        this.shopDetailsActivity=shopDetailsActivity;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getContext();
        view=inflater.inflate(R.layout.shopdetails_frag3, container, false);
        recyclerView=view.findViewById(R.id.recyclerView_Frag3);
        customRecyclerViewAdapterFrag3Reviews=new CustomRecyclerViewAdapterFrag3Reviews(getContext(),shopDetailsActivity.aShop);
        recyclerView.setAdapter(customRecyclerViewAdapterFrag3Reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addReviewButtonFrag3 =view.findViewById(R.id.AddReviewButton_Frag3);
        addReviewButtonFrag3.setOnClickListener(new View.OnClickListener() {
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
                      shopDetailsActivity.addReview(ReviewerNameEditText.getText().toString(), ReviewerCommentEditText.getText().toString(), RatingBar.getRating());
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
    public  void ReceivedNewReviewsNotifyRecyclerView(){
        customRecyclerViewAdapterFrag3Reviews=new CustomRecyclerViewAdapterFrag3Reviews( mContext,shopDetailsActivity.aShop);
        recyclerView.swapAdapter(customRecyclerViewAdapterFrag3Reviews, true);
    }
}
