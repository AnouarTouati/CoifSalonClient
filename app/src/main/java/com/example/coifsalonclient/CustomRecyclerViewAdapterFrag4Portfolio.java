package com.example.coifsalonclient;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class CustomRecyclerViewAdapterFrag4Portfolio extends RecyclerView.Adapter<CustomRecyclerViewAdapterFrag4Portfolio.ViewHolder> {

    ArrayList<Bitmap> portfolioImages =new ArrayList();
    Context mContext;

    public CustomRecyclerViewAdapterFrag4Portfolio(ArrayList<Bitmap> portfolioImages, Context mContext) {
        this.portfolioImages = portfolioImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CustomRecyclerViewAdapterFrag4Portfolio.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_portfolio_recyclerview_item_frag4, viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerViewAdapterFrag4Portfolio.ViewHolder viewHolder, int i) {
       viewHolder.portfolioImageViewFrag4.setImageBitmap(portfolioImages.get(i));
    }

    @Override
    public int getItemCount() {
        return portfolioImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView portfolioImageViewFrag4;
        ConstraintLayout portfolioConstraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            portfolioImageViewFrag4=itemView.findViewById(R.id.portfolioImageView_Frag4);
            portfolioConstraintLayout=itemView.findViewById(R.id.portfolioConstraintLayout);
        }
    }
}
