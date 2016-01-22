package ben.practice.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import ben.practice.R;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class Util {


    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        return width;
    }

    public static int getViewWidth(View view){
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w,h);
        int width = view.getMeasuredWidth();
//        System.out.println("measure width=" + width + " height=" + height);
        return width;
    }

    public static int getImageWidth(Context context,int imageId){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imageId, opts);
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;
        int width=opts.outWidth;
        int height=opts.outHeight;

        return width;
    }

    public static int getImageHeight(Context context,int imageId){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imageId, opts);
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;
        int height=opts.outHeight;
        return height;
    }

    public static int getViewHeight(View view){
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
//        System.out.println("measure width=" + width + " height=" + height);
        return height;
    }

    public static int getScreendensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        return (int)density;
    }

    public static void printlnViewSize(View view){
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        System.out.println("measure width=" + width + " height=" + height);
    }

    public static void println(Object string){
        System.out.println(string);
    }

    public static StateListDrawable newSelector(Context context,int idNormal, int idPressed) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);

        stateListDrawable.addState(new int[]{}, normal);
        return stateListDrawable;
    }
}
