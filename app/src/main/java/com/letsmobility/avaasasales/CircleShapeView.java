package com.letsmobility.avaasasales;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.ImageViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;



public class CircleShapeView extends LinearLayout {
    ImageView imageView;
    int backgroundColor;
    int imageDrawable;
    int borderColor;

    public CircleShapeView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleShapeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleShapeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBackground(getCircleBackground());
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
        if (imageView == null) {
            imageView = new ImageView(getContext());
            addView(imageView);
        }
        imageView.setImageResource(imageDrawable);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleShapeView);

            backgroundColor = attributes.getColor(R.styleable.CircleShapeView_background_circle, Color.WHITE);
            borderColor = attributes.getColor(R.styleable.CircleShapeView_stroke_color, backgroundColor);
            imageDrawable = attributes.getResourceId(R.styleable.CircleShapeView_center_image, -1);
            if (imageDrawable != -1) {
                imageView = new ImageView(getContext());
                imageView.setImageResource(imageDrawable);
                addView(imageView);
            } else {
                if (imageView != null) {
                    removeView(imageView);
                }
            }
            setBackground(getCircleBackground());
        }

    }

    private Drawable getCircleBackground() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(backgroundColor);
        shape.setStroke(3, borderColor);
        return shape;
    }

    public void setImageTint(int color) {
        ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color));
    }
}
