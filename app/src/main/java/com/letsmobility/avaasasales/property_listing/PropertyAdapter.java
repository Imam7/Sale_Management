package com.letsmobility.avaasasales.property_listing;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.letsmobility.avaasasales.CreateVisitActivity;
import com.letsmobility.avaasasales.R;
import com.letsmobility.avaasasales.image.PicassoManager;
import com.letsmobility.avaasasales.model.Visitdetail;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

    private List<Visitdetail> visitdetailList;
    Context mContext;

    PropertyAdapter(List<Visitdetail> visitdetailList, Context context) {
        mContext = context;
        this.visitdetailList = visitdetailList;
    }

    @Override
    public int getItemCount() {
        return visitdetailList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hostel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int listPosition) {

        final Visitdetail visit = visitdetailList.get(listPosition);

        holder.mTextName.setText(visit.getPropertyName());
        holder.mArea.setText(visit.getArea());
        holder.mTextAddress.setText(visit.getPropertyAddress());
        holder.mTextRating.setText("Rating : " + visit.getRate() + "/5");
        holder.imgFavourite.setVisibility(visit.getIsLikeProduct().equals("Yes") ? View.VISIBLE : View.INVISIBLE);
        holder.tvNeedToFollow.setVisibility(visit.getIsLikeProduct().equals("Need to Follow up") ? View.VISIBLE : View.INVISIBLE);

        if (visit.getPropertyPhotosUrl() != null && visit.getPropertyPhotosUrl().size()>0)
            PicassoManager.loadLocalImage(mContext, holder.mImageHostel, visit.getPropertyPhotosUrl().get(0));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateVisitActivity.class);
                intent.putExtra("property_data", visit);
                mContext.startActivity(intent);
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextAddress, mArea;
        TextView mTextName;
        ImageView mImageHostel, imgFavourite;
        TextView mTextRating, tvNeedToFollow;


        ViewHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.lm_text_name);
            mTextAddress = itemView.findViewById(R.id.lm_text_hostel_address);
            mTextRating = itemView.findViewById(R.id.lm_text_hostel_rating);
            mImageHostel = itemView.findViewById(R.id.lm_image_hostel);
            imgFavourite = itemView.findViewById(R.id.lm_image_favourite);
            mArea = itemView.findViewById(R.id.tv_area);
            tvNeedToFollow = itemView.findViewById(R.id.tv_need_to_follow);
        }


    }

}