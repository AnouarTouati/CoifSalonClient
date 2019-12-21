package com.example.coifsalonclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.coifsalonclient.R.layout.store_reviews_recyclerview_item_frag3;

public class CustomRecyclerViewAdapterFrag3Reviews extends RecyclerView.Adapter<CustomRecyclerViewAdapterFrag3Reviews.ViewHolder> {

    public ArrayList<String> reviewersNames=new ArrayList<>();
    public ArrayList<String> reviewersComments=new ArrayList<>();
    public ArrayList<String> reviewersCommentDate=new ArrayList<>();
    public ArrayList<Float> reviewersGivenStars=new ArrayList<>();
    public Context mContext;

    public CustomRecyclerViewAdapterFrag3Reviews(ArrayList<String> reviewersNames, ArrayList<String> reviewersComments, ArrayList<String> reviewersCommentDate, ArrayList<Float> reviewersGivenStars, Context mContext) {
        this.reviewersNames = reviewersNames;
        this.reviewersComments = reviewersComments;
        this.reviewersCommentDate = reviewersCommentDate;
        this.reviewersGivenStars = reviewersGivenStars;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(store_reviews_recyclerview_item_frag3, viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       viewHolder.reviewerName.setText(reviewersNames.get(i));
       viewHolder.reviewerComment.setText(reviewersComments.get(i));
       viewHolder.reviewerCommentDate.setText(reviewersCommentDate.get(i));
       viewHolder.ratingBar.setNumStars(5);
       viewHolder.ratingBar.setRating(reviewersGivenStars.get(i));


    }

    @Override
    public int getItemCount() {
        return reviewersNames.size();
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
            recyclerViewPeopleReviewsItemLayout=itemView.findViewById(store_reviews_recyclerview_item_frag3);
        }
    }
}
