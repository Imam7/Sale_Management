package com.letsmobility.avaasasales.image;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.letsmobility.avaasasales.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MultiImageSelectAdapter extends RecyclerView.Adapter<MultiImageSelectAdapter.ViewHolder> {

    ArrayList<String> mList = new ArrayList<>();
    LayoutInflater mInflater;
    Context mContext;
    SparseBooleanArray mSparseBooleanArray;
    int deviceWidth;
    int selectImageLimit = 1;
    String errorMessage = " can be selected";
    //    private GlideManager glideManager;
    private Picasso picassoInstance;
    private boolean isVideo;

    public MultiImageSelectAdapter(Context context, ArrayList<String> imageList, int deviceWidth, boolean isVideo) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mSparseBooleanArray = new SparseBooleanArray();
        mList = imageList;
        this.isVideo = isVideo;
        this.deviceWidth = deviceWidth / 3;
//        glideManager = GlideManager.getInstance(context.getApplicationContext());
//        VideoRequestHandler videoRequestHandler = new VideoRequestHandler();
//        picassoInstance = new Picasso.Builder(context.getApplicationContext())
//                .addRequestHandler(videoRequestHandler)
//                .build();
    }

    public void setSelectImageLimit(int selectImageLimit) {
        this.selectImageLimit = selectImageLimit;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPreview;
        ImageView imgChecked;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPreview = itemView.findViewById(R.id.imageView1);
            imgChecked = (ImageView) itemView.findViewById(R.id.imgChecked);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_image_picker, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mList != null) {
            String image = mList.get(position);
            if (image != null) {
//                glideManager.loadImageFromLocal(mList.get(position), holder.imgPreview);
                if (isVideo) {
                  //  picassoInstance.load(VideoRequestHandler.SCHEME_VIDEO + ":" + mList.get(position)).into(holder.imgPreview);
                } else {
                    PicassoManager.loadLocalImage(mContext, holder.imgPreview, mList.get(position));
                }

//                Glide.with(mContext.getApplicationContext())
//                        .load("file://" + mList.get(position))
//                        .thumbnail(0.25f)
//                        .into(holder.imgPreview);

                holder.imgChecked.setTag(position);
                holder.imgChecked.setVisibility(mSparseBooleanArray.get(position) ? View.VISIBLE : View.GONE);
                holder.itemView.setSelected(mSparseBooleanArray.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getCheckedItems().size() == selectImageLimit && !view.isSelected()) {
                            if (selectImageLimit == 1) {
                                Toast.makeText(mContext, "Only " + selectImageLimit + errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Only " + selectImageLimit + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            boolean selected = view.isSelected();
                            holder.imgChecked.setVisibility(!selected ? View.VISIBLE : View.GONE);
                            view.setSelected(!selected);
                            mSparseBooleanArray.put((Integer) holder.imgChecked.getTag(), !selected);
                        }
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        else
            return 0;
    }

    public ArrayList<String> getCheckedItems() {
        ArrayList<String> selectedImages = new ArrayList<String>();
        for (int i = 0; i < mList.size(); i++) {
            if (mSparseBooleanArray.get(i)) {
                selectedImages.add(mList.get(i));
            }
        }
        return selectedImages;
    }
}
