package ben.practice.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import ben.practice.R;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class RestDialog {
    private final Dialog mDialog;
    private TextView rest_navigation;
    private TextView rest_text;

    public RestDialog(Context context) {
        LayoutInflater mInflater;
        mDialog = new Dialog(context, R.style.edit_AlertDialog_style);
        mInflater = LayoutInflater.from(context);
        final View dialogView = mInflater.inflate(R.layout.dialog_rest, null);
        final Window window = mDialog.getWindow();
        window.setContentView(dialogView);
        final WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (Util.getScreenWidth(context) * 0.8);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        findView(window);
    }

    private void findView(Window window) {
        rest_navigation = (TextView) window.findViewById(R.id.rest_navigation);
        rest_text = (TextView) window.findViewById(R.id.rest_text);
    }

    public void setText(String text) {
        rest_text.setText(text);
    }

    public void setRestNavigationOnClick(View.OnClickListener listener) {
        rest_navigation.setOnClickListener(listener);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }
}
