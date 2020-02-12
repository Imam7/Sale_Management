package com.letsmobility.avaasasales.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.letsmobility.avaasasales.BuildConfig;
import com.letsmobility.avaasasales.CircleShapeView;
import com.letsmobility.avaasasales.R;
import com.letsmobility.avaasasales.image.MultiImageSelectActivity;

import java.io.File;

public class ImagePickerUtils {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1886;
    public static final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 1888;


    public File photoFile;
    private Uri fileUri;
    private boolean isVideoDisabled;

    public ImagePickerUtils() {

    }

    public void setVideoDisabled(boolean videoDisabled) {
        isVideoDisabled = videoDisabled;
    }

    public File getPhotoFile() {
        return photoFile;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void broadcastMediaScanner(AppCompatActivity activity) {
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
    }

    public void broadcastMediaScanner(AppCompatActivity activity, Uri uri) {
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

    public void dispatchTakePhotoIntent(Fragment fragment) {
        if (!PermissionUtils.verifyCameraStoragePermissions(fragment)) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = getImagePath();
        if (photoFile != null) {
            this.photoFile = photoFile;
            fileUri = FileProvider.getUriForFile(fragment.getContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            fragment.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }



    public void selectImageFromGallery(Fragment fragment, boolean singleImage) {
        if (!PermissionUtils.verifyStoragePermissions(fragment)) {
            return;
        }
        Intent intentGallery = new Intent(fragment.getActivity(), MultiImageSelectActivity.class);
        intentGallery.putExtra("SELECT_IMG_LIMIT", singleImage ? 1 : 10);
        intentGallery.putExtra("selectionType", isVideoDisabled ? MultiImageSelectActivity.PICK_IMAGE : MultiImageSelectActivity.PICK_ALL);
        fragment.startActivityForResult(
                intentGallery,
                SELECT_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void selectImageFromGallery(Fragment fragment) {
        if (!PermissionUtils.verifyStoragePermissions(fragment)) {
            return;
        }
        Intent intentGallery = new Intent(fragment.getActivity(), MultiImageSelectActivity.class);
        intentGallery.putExtra("SELECT_IMG_LIMIT", 10);
        intentGallery.putExtra("selectionType", isVideoDisabled ? MultiImageSelectActivity.PICK_IMAGE : MultiImageSelectActivity.PICK_ALL);
        fragment.startActivityForResult(
                intentGallery,
                SELECT_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    private File getImagePath() {
        File gallery = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        return new File(gallery.getPath() + File.separator +
                "cards_" + System.currentTimeMillis() + ".jpg");
    }


    public void showDialog(final Fragment fragment, final boolean isSingleImage) {
        showDialog(fragment, isSingleImage, false);
    }


    public void showDialog(final Fragment fragment, final boolean isSingleImage, final boolean isOnlyVideo) {
        AlertDialog.Builder diaBuilder = new AlertDialog.Builder(fragment.getContext());

        LayoutInflater inflater = (LayoutInflater) fragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.dialog_image_picker, null, false);

        diaBuilder.setView(customView);
        final AlertDialog alertDialog = diaBuilder.create();
        alertDialog.show();
        CircleShapeView ivCamera = customView.findViewById(R.id.iv_cam);
        CircleShapeView ivGallery = customView.findViewById(R.id.iv_gal);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnlyVideo) {
                } else {
                    dispatchTakePhotoIntent(fragment);
                }
                alertDialog.dismiss();

            }
        });
        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnlyVideo) {
                //    selectVideo(fragment);
                } else {
                    if (!isSingleImage) {
                        selectImageFromGallery(fragment, false);
                    } else {
                        selectImageFromGallery(fragment, true);
                    }
                }
                alertDialog.dismiss();

            }
        });
        alertDialog.show();
    }


    public void showDialog(final Fragment fragment) {
        final Dialog dialog = new Dialog(fragment.getActivity());
        dialog.setContentView(R.layout.dialog_image_picker);
        CircleShapeView ivCamera = dialog.findViewById(R.id.iv_cam);
        CircleShapeView ivGallery = dialog.findViewById(R.id.iv_gal);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePhotoIntent(fragment);
                dialog.dismiss();

            }
        });
        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageFromGallery(fragment);
                dialog.dismiss();

            }
        });
        dialog.show();
    }
}
