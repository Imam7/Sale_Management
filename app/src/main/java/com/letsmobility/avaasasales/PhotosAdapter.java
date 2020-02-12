package com.letsmobility.avaasasales;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyPhotoViewHolder> {
    private List<File> mImageFiles;
    private Context mContext;
    private ItemRemoveListener mListener;

    interface ItemRemoveListener{
        void onRemoveClick( int position);
    }


    public PhotosAdapter(Context context, List<File> imageFiles, ItemRemoveListener listener) {
        mContext = context;
        mImageFiles = imageFiles;
        mListener = listener;
    }

    public void setData(List<File> ImageFiles) {
        mImageFiles = mImageFiles;
        notifyDataSetChanged();
    }

    @Override
    public MyPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent,false);
        return new MyPhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyPhotoViewHolder holder, final int position) {
        Picasso.get().load(mImageFiles.get(position)).fit().centerCrop()
                .placeholder(R.drawable.ic_photo_grey)
                .error(R.drawable.ic_photo_grey)
                .into(holder.ivPhoto);
        holder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageFiles.remove(mImageFiles.get(position));
                notifyItemRemoved(position);
                notifyDataSetChanged();
                mListener.onRemoveClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageFiles != null ? mImageFiles.size() : 0;
    }

    public class MyPhotoViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView ivPhoto,ivClose;

        public MyPhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            ivClose = itemView.findViewById(R.id.iv_close);
        }
    }
}
