package com.letsmobility.avaasasales.image;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.letsmobility.avaasasales.R;
import com.letsmobility.avaasasales.custom_view.FontFitTextView;
import com.letsmobility.avaasasales.utils.AndroidUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MultiImageSelectActivity extends AppCompatActivity {

    private ArrayList<String> imageUrls;
    private ArrayList<String> videoUrls;
    private ArrayList<Long> videoIds;
    private MultiImageSelectAdapter imageAdapter;
    final private int MAX_COULMNS = 3;
    int deviceWidth;
    int selectImageLimit;
    TabLayout tabLayout;
    RecyclerView gridView;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final int PICK_ALL = 0;
    public static final int PICK_IMAGE = 1;
    public static final int PICK_VIDEO = 2;

    private int selectionType = PICK_ALL;

    public static final String SELECTED_MEDIA_TYPE = "MediaType";
    private int selectedMediaType = 0;
    private String imageSelectionError = " images can be selected";
    private String videoSelectionError = " video can be selected";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        Bundle bundle = getIntent().getExtras();
        selectImageLimit = bundle.getInt("SELECT_IMG_LIMIT");
        selectionType = bundle.getInt("selectionType", 0);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_file);
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    if (selectionType == PICK_ALL || selectionType == PICK_IMAGE) {
                        selectedMediaType = MEDIA_TYPE_IMAGE;
                        getImageUri();
                        setDataSource(imageUrls, selectImageLimit, imageSelectionError, false);
                    } else {
                        selectedMediaType = MEDIA_TYPE_VIDEO;
                        getVideoUri();
                        setDataSource(videoUrls, 1, videoSelectionError, true);
                    }
                } else {
                    selectedMediaType = MEDIA_TYPE_VIDEO;
                    getVideoUri();
                    setDataSource(videoUrls, 1, videoSelectionError, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        deviceWidth = (int) (AndroidUtil.getDisplayWidth(this) - (AndroidUtil.convertDpToPixel(this, 16f)));
        gridView = (RecyclerView) findViewById(R.id.gridview);
        gridView.addItemDecoration(new SpacesItemDecoration(8));
        gridView.setLayoutManager(new GridLayoutManager(this, MAX_COULMNS));
        if (selectionType == PICK_ALL || selectionType == PICK_IMAGE) {
            selectedMediaType = MEDIA_TYPE_IMAGE;
            getImageUri();
            setDataSource(imageUrls, selectImageLimit, imageSelectionError, false);
        } else {
            selectedMediaType = MEDIA_TYPE_VIDEO;
            getVideoUri();
            setDataSource(videoUrls, 1, videoSelectionError, true);
        }
    }

    private void setDataSource(ArrayList<String> pathList, int selectionLimit, String errorMessage, boolean isVideo) {
        imageAdapter = new MultiImageSelectAdapter(this, pathList, deviceWidth, isVideo);
        imageAdapter.setSelectImageLimit(selectionLimit);
        imageAdapter.setErrorMessage(errorMessage);
        gridView.setAdapter(imageAdapter);
    }

    private void getImageUri() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = getApplicationContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");
        this.imageUrls = new ArrayList<String>();
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int column_index = imagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(column_index));

        }
        imagecursor.close();
    }

    private void getVideoUri() {
        final String[] columns = {MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID};
        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        Cursor videoCursor = getApplicationContext().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");
        this.videoUrls = new ArrayList<String>();
        for (int i = 0; i < videoCursor.getCount(); i++) {
            videoCursor.moveToPosition(i);
            int column_index = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int id_index = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            videoUrls.add(videoCursor.getString(column_index));
//            videoIds.add(videoCursor.getLong(id_index));
        }
        videoCursor.close();
    }

    private void setupTabIcons() {
        Map<String, Integer> tabvalues = new LinkedHashMap<>();
        if (selectionType == 0 || selectionType == 1)
            tabvalues.put("Photos", R.drawable.selector_images);
        if (selectionType != 1) {
            tabvalues.put("Videos", R.drawable.selector_video);
        }
        int i = 0;
        for (Map.Entry<String, Integer> entry : tabvalues.entrySet()) {
            try {
                FontFitTextView tabView = (FontFitTextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                tabView.setText(entry.getKey());
                tabView.setCompoundDrawablePadding(10);
                tabView.setCompoundDrawablesWithIntrinsicBounds(0, entry.getValue(), 0, 0);
                if (i == 0) {
                    tabView.setSelected(true);
                }
                tabLayout.addTab(tabLayout.newTab().setCustomView(tabView));
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        tabLayout.getTabAt(0).select();
    }

    @Override
    protected void onStop() {
//        imageLoader.stop();
        super.onStop();
    }

    public void btnChoosePhotosClick(View v) {
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
        Log.d(MultiImageSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("selectedImages", selectedItems);
        if (selectedMediaType == MEDIA_TYPE_IMAGE || selectedMediaType == MEDIA_TYPE_VIDEO) {
            resultIntent.putExtra(SELECTED_MEDIA_TYPE, selectedMediaType);
        }
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) < MAX_COULMNS)
                outRect.top = space;
        }
    }
}
