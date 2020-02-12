package com.letsmobility.avaasasales.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.letsmobility.avaasasales.R;

public class PermissionUtils {

    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_CAMERA = 2;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean verifyStoragePermissions(Fragment fragment) {
        return verifyStoragePermissions(fragment.getActivity(), fragment);
    }

    public static boolean verifyCameraStoragePermissions(Fragment fragment) {
        return verifyCameraStoragePermissions(fragment.getActivity(), fragment);
    }



    //Private
    private static void askPermissions(Activity activity, Fragment fragment, String[] permissions, int requestCode) {
        if (fragment != null) {
            fragment.requestPermissions(permissions, requestCode);
        } else {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    private static boolean verifyStoragePermissions(Activity activity, Fragment fragment) {
        String storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int permission = ActivityCompat.checkSelfPermission(activity, storagePermission);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, storagePermission);
            if (showRationale) {
                //Show dialog to explain about the permission
                askPermissions(activity, fragment, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

            } else {
                if (!isFirstTimeAskingPermission(activity, storagePermission)) {
                    promptSettings(activity, R.string.photos_permission);
                } else {
                    firstTimeAskingPermission(activity, storagePermission);
                    askPermissions(activity, fragment, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                }
            }
        }
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean verifyCameraStoragePermissions(Activity activity, Fragment fragment) {
        int cameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int storagePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (cameraPermission != PackageManager.PERMISSION_GRANTED
                || storagePermission != PackageManager.PERMISSION_GRANTED) {

            boolean showStorageRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            boolean showCameraRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA);

            if (showStorageRationale && showCameraRationale) {
                askPermissions(activity, fragment, PERMISSIONS_CAMERA, REQUEST_CAMERA);
            } else {
                if (cameraPermission != PackageManager.PERMISSION_GRANTED && !showCameraRationale
                        && !isFirstTimeAskingPermission(activity, Manifest.permission.CAMERA)) {
                    promptSettings(activity, R.string.camera_permission);
                } else if (storagePermission != PackageManager.PERMISSION_GRANTED && !showStorageRationale
                        && !isFirstTimeAskingPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    promptSettings(activity, R.string.photos_permission);
                } else {
                    firstTimeAskingPermission(activity, Manifest.permission.CAMERA);
                    firstTimeAskingPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    askPermissions(activity, fragment, PERMISSIONS_CAMERA, REQUEST_CAMERA);
                }
            }
        }
        return (cameraPermission == PackageManager.PERMISSION_GRANTED
                && storagePermission == PackageManager.PERMISSION_GRANTED);
    }



    private static boolean isFirstTimeAskingPermission(Context context, String permission) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return prefs.getBoolean(permission, true);
    }

    private static void firstTimeAskingPermission(Context context, String permission) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        prefs.edit().putBoolean(permission, false).apply();
    }

    private static void promptSettings(final Activity activity, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle(activity.getString(R.string.settings_title));
        builder.setMessage(message);
        builder.setNegativeButton(activity.getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goToSettings(activity);
            }
        });
        builder.setPositiveButton(activity.getString(R.string.action_dismiss), null);
        builder.show();
    }

    private static void goToSettings(Activity activity) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(myAppSettings);
    }

}
