package ben.practice.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import ben.practice.R;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class PhotoDialog {
    private final Dialog mDialog;

    private TextView camera;
    private TextView album;
    public PhotoDialog(Context context){
        LayoutInflater mInflater;
        mDialog = new Dialog(context, R.style.edit_AlertDialog_style);
        mInflater = LayoutInflater.from(context);
        final View dialogView = mInflater.inflate(R.layout.dialog_photo, null);
        final Window window = mDialog.getWindow();
        window.setContentView(dialogView);
        final WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (Util.getScreenWidth(context) * 0.8);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        findView(window);
    }

    private void findView(Window window) {
        camera = (TextView) window.findViewById(R.id.camera);
        album = (TextView) window.findViewById(R.id.album);

    }

    public void setTopBtnOnClick(View.OnClickListener listener) {
        camera.setOnClickListener(listener);
    }

    public void setBottomBtnOnClick(View.OnClickListener listener) {
        album.setOnClickListener(listener);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }
}
