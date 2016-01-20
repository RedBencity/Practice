package ben.practice.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import ben.practice.MainActivity;
import ben.practice.R;

/**
 * Created by Administrator on 2016/1/20 0020.
 */
public class MyTimePickerDialog {
    private final Dialog mDialog;
    private Button time_picker_left_btn;
    private Button time_picker_right_btn;
    private TimePicker time_picker;
    private int hour = 0;
    private int minutes = 0;

    public MyTimePickerDialog(Context context) {
        LayoutInflater mInflater;
        mDialog = new Dialog(context, R.style.edit_AlertDialog_style);
        mInflater = LayoutInflater.from(context);
        final View dialogView = mInflater.inflate(R.layout.dialog_time_picker, null);
        final Window window = mDialog.getWindow();
        window.setContentView(dialogView);
        final WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (Util.getScreenWidth(context) * 0.8);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        findView(window);
        setTimePicker();
    }

    private void findView(Window window) {
        time_picker = (TimePicker) window.findViewById(R.id.time_picker);
        time_picker_left_btn = (Button) window.findViewById(R.id.time_picker_left_btn);
        time_picker_right_btn = (Button) window.findViewById(R.id.time_picker_right_btn);

    }

    private void setTimePicker() {
        time_picker.setIs24HourView(true);
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);
        time_picker.setCurrentHour(currentHour);
        time_picker.setCurrentMinute(currentMinute);
        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minutes = minute;
            }
        });
    }

    public void setTimeFromSave(int hour, int minute){
        time_picker.setCurrentHour(hour);
        time_picker.setCurrentMinute(minute);
    }

    public int getHour(){

        return hour;
    }
    public int getMinute(){
        return minutes;
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void setLeftBtnOnClick(View.OnClickListener listener) {
        time_picker_left_btn.setOnClickListener(listener);
    }

    public void setRightBtnOnClick(View.OnClickListener listener) {
        time_picker_right_btn.setOnClickListener(listener);
    }
}
