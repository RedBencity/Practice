package ben.practice.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Environment;
import android.text.Editable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ben.practice.R;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class Util {

    //关闭键盘
    public static void colseKeybord(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    //获取屏幕宽
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        return width;
    }

    //获取View宽
    public static int getViewWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
//        System.out.println("measure width=" + width + " height=" + height);
        return width;
    }

    //获取图片宽
    public static int getImageWidth(Context context, int imageId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imageId, opts);
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;
        int width = opts.outWidth;
        int height = opts.outHeight;

        return width;
    }

    //获取图片宽
    public static int getImageHeight(Context context, int imageId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imageId, opts);
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;
        int height = opts.outHeight;
        return height;
    }

    //获取View高
    public static int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
//        System.out.println("measure width=" + width + " height=" + height);
        return height;
    }

    //获取屏幕密度
    public static int getScreenDensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        return (int) density;
    }

    public static void printlnViewSize(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        System.out.println("measure width=" + width + " height=" + height);
    }

    public static void println(Class c,Object string) {
        String className = c.getName();
        System.out.println(className+"------->"+ string);
    }

    public static void println(Object o,Object string) {
        String className = o.getClass().getName();
        System.out.println(className+"------->"+ string);
    }

    public static void println(Object string) {
        System.out.println(string);
    }

    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);

        stateListDrawable.addState(new int[]{}, normal);
        return stateListDrawable;
    }



    public static void setToast(Activity activity, String str) {
        Toast toast = Toast.makeText(activity, str, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void setToast(Activity activity, String str,int duration) {
        Toast toast = Toast.makeText(activity, str, duration);
        toast.show();
    }

    // 字符串是否为空（全是不可见字符的字符串认为是空）
    public static boolean isStrEmpty(Editable poStr) {
        String lsStr = poStr.toString();
        return isStrEmpty(lsStr);
    }

    // 字符串是否为空（全是不可见字符的字符串认为是空）
    public static boolean isStrEmpty(String psStr) {
        return psStr == null || psStr.trim().length() == 0;
    }

    //判断SD卡有无选择路径
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static String getDiskCacheDirName(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        System.out.println("-------------------" + cachePath + File.separator + uniqueName);
        return (cachePath + File.separator + uniqueName);
    }

    public static boolean isFile(String path){
        File file=new File(path);
        if(file.exists())
        {
            return true;
        }
            return false;
    }
    public static String getPhotoPath(Context context,String fileName){
        String path =  Util.getDiskCacheDirName(context, "practice")+File.separator+ fileName+".png";
        return path;
    }


}
