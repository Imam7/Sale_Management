package com.letsmobility.avaasasales.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class PicassoManager {

    public static void loadNetworkImage(Context context, ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            Picasso.get()
                    .load(url)
                    //   .fit()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void loadNetworkImage(Context context, ImageView imageView, String url, int placeHolder) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            Picasso.get()
                    .load(url)
                    .placeholder(placeHolder)
                    // .fit()
                    .into(imageView);

        } catch (Exception e) {

        }

    }

    public static void loadNetworkImageRoundedCorners(Context context, ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Picasso.get()
                .load(url)
                .transform(new RoundedCornersTransformation(20, 0,
                        RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }

    public static void loadNetworkImageRoundedCorners(Context context, ImageView imageView, String url, int placeHolder) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Picasso.get()
                .load(url)
                .placeholder(placeHolder)
                .transform(new RoundedCornersTransformation(8, 0,
                        RoundedCornersTransformation.CornerType.ALL))
                .into(imageView);
    }

    public static void loadNetworkImageCircular(Context context, ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Picasso.get()
                .load(url)
                .transform(new CropCircleTransformation())
                .into(imageView);
    }

    public static void loadNetworkImageCircular(Context context, ImageView imageView, String url, int placeHolder) {
        try {
            Picasso.get()
                    .load(url)
                    .placeholder(placeHolder)
                    .transform(new CropCircleTransformation())
                    .into(imageView);
        } catch (Exception e) {

        }
    }

    public static void loadLocalImage(Context context, ImageView imageView, String path) {
        try {
            Picasso.get()
                    .load("file://" + path)
                    .config(Bitmap.Config.RGB_565)
                    .fit()
                    //.centerCrop()
                    .into(imageView);
        } catch (Exception e) {

        }
    }


    public static void loadLocalImage(Context context, ImageView imageView, File file) {
        try {
            Picasso.get()
                    .load(file)
                    .config(Bitmap.Config.RGB_565)
                    .fit()
                    //.centerCrop()
                    .into(imageView);
        } catch (Exception e) {

        }
    }

    public static void loadLocalImageWithOutScaling(Context context, ImageView imageView, File file) {
        try {
            Picasso.get()
                    .load(file)
                    .into(imageView);
        } catch (Exception e) {
        }
    }
}
