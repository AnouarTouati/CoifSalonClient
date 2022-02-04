package com.example.coifsalonclient.shopdetails;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import static com.example.coifsalonclient.R.layout.shop_reviews_recyclerview_item_frag3;

import com.example.coifsalonclient.AShop;
import com.example.coifsalonclient.R;

public class CustomRecyclerViewAdapterFrag3Reviews extends RecyclerView.Adapter<CustomRecyclerViewAdapterFrag3Reviews.ViewHolder> {

    AShop aShop;
    public Context mContext;

    public CustomRecyclerViewAdapterFrag3Reviews(Context mContext,AShop aShop) {
        this.aShop=aShop;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(shop_reviews_recyclerview_item_frag3, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if(aShop.hasLoadedReviews()){
            viewHolder.reviewerName.setText(aShop.getReviewersNames().get(i));
            viewHolder.reviewerComment.setText(aShop.getReviewersComments().get(i));
            viewHolder.reviewerCommentDate.setText(aShop.getReviewersCommentDate().get(i));
            viewHolder.ratingBar.setNumStars(5);
            viewHolder.ratingBar.setRating(aShop.getReviewersGivenStars()[i]);
        }


    }

    @Override
    public int getItemCount() {
        if(aShop.hasLoadedReviews()){
            return aShop.getReviewersNames().size();
        }else{
            return 0;
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView reviewerName;
        TextView reviewerComment;
        TextView reviewerCommentDate;
        RatingBar ratingBar;
        ConstraintLayout recyclerViewPeopleReviewsItemLayout;
        @SuppressLint("ResourceType")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewerName=itemView.findViewById(R.id.reviewerName);
            reviewerComment=itemView.findViewById(R.id.reviewerComment);
            reviewerCommentDate=itemView.findViewById(R.id.reviewerCommentDate);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            recyclerViewPeopleReviewsItemLayout=itemView.findViewById(shop_reviews_recyclerview_item_frag3);
        }
    }
}
