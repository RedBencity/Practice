package ben.practice.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class Util {


    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        Log.i("width", width + "");
        return width;
    }
}
