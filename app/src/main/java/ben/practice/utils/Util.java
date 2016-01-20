package ben.practice.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class Util {


    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
//        Log.i("width", width + "");
        return width;
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
