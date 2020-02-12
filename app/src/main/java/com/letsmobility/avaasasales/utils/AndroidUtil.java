package com.letsmobility.avaasasales.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


public class AndroidUtil {
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    /**
     * @param activity
     */
    public static void hideKeyBoard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
        if (view == null) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * @param activity
     */
    public static void hideKeyBoard(Activity activity) {
        hideKeyBoard(activity, null);
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                editText.requestFocus();
                editText.setFocusableInTouchMode(true);
                editText.setSelection(editText.length());
            }
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public static int convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp / (metrics.densityDpi / 160f));
    }

    public static float pxToDp(Context context, float myPixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, myPixels, context.getResources().getDisplayMetrics());
    }

    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());

    }

    public static int getDisplayWidth(Context context) {
        if (context != null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return displayMetrics.widthPixels;
        }
        return 0;
    }

    // DeviceDimensionsHelper.getDisplayHeight(context) => (display height in pixels)
    public static int getDisplayHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    public static void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * Converts pixels into dip.
     *
     * @param context
     * @param px
     * @return
     */
    public int convertPixelToDP(Context context, int px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (px * (metrics.densityDpi / 160f));
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }

    public static String getRealPathFromURI(Activity activity, Uri contentURI) {
        String result;
        String[] column = new String[2];
//        new AppLog().info("Utils uri path",""+contentURI);
        if (true) {
            column[0] = MediaStore.Images.Media.DATA;
            column[1] = MediaStore.Images.Media.SIZE;
        } else {
            column[0] = MediaStore.Video.VideoColumns.DATA;
            column[1] = MediaStore.Video.VideoColumns.SIZE;
        }
        Cursor cursor = activity.getContentResolver().query(contentURI, column, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            result = cursor.getString(0);

            cursor.close();
        }
        return result;
    }

    public static String getVideoPathFromUri(Context context, Uri uri) {
        String result;
        String[] column = new String[2];
        column[0] = MediaStore.Video.VideoColumns.DATA;
        column[1] = MediaStore.Video.VideoColumns.SIZE;
        Cursor cursor = context.getContentResolver().query(uri, column, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            result = cursor.getString(0);
            cursor.close();
        }
        return result;
    }

}
