package ben.practice.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ben.practice.R;
import ben.practice.broadcast.AlarmReceiver;
import ben.practice.entity.AlarmClockInfo;
import ben.practice.utils.MyTimePickerDialog;
import ben.practice.utils.Util;

public class AlarmClockActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView alarm_clock_listview;
    private AlarmClockAdapter alarmClockAdapter;
    private List<AlarmClockInfo> alarm_clock_times;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        alarm_clock_times = new ArrayList<AlarmClockInfo>();
        preferences = getSharedPreferences("alarmclocklist", MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void getData() {
        alarm_clock_listview = (ListView) findViewById(R.id.alarm_clock_listview);
        alarmClockAdapter = new AlarmClockAdapter();
        AlarmClockInfo alarmClockInfo;
        for (int i = 0; i < preferences.getInt("length", 0); i++) {
            alarmClockInfo = new AlarmClockInfo();
            alarmClockInfo.setTime(preferences.getString("time" + i, null));
            alarmClockInfo.setState(preferences.getBoolean("state" + i, true));
            alarm_clock_times.add(alarmClockInfo);
        }
        alarm_clock_listview.setAdapter(alarmClockAdapter);
    }


    private void setToolbar() {
        View view = findViewById(R.id.alarm_clock_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("每日练习提醒");
        ImageView head_right_btn = (ImageView) view.findViewById(R.id.head_right_btn);
        head_right_btn.setVisibility(View.VISIBLE);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setContentInsetsRelative(0, 0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
            }
        });

        head_right_btn.setBackgroundDrawable(Util.newSelector(AlarmClockActivity.this,R.mipmap.bar_add, R.mipmap.bar_add_pressed));
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        head_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(AlarmClockActivity.this);
                timePickerDialog.setLeftBtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar currentTime = Calendar.getInstance();
                        int hourOffset = timePickerDialog.getHour() - currentTime.get(Calendar.HOUR_OF_DAY);
                        int minuteOffset = timePickerDialog.getMinute() - currentTime.get(Calendar.MINUTE);
                        long firstTime = setFirstTime(hourOffset, minuteOffset);
                        String time = getTime(timePickerDialog, hourOffset, minuteOffset);
                        AlarmClockInfo alarmClockInfo = new AlarmClockInfo();
                        alarmClockInfo.setTime(time);
                        alarmClockInfo.setState(true);
                        int length = preferences.getInt("length", 0);
                        editor.putInt("length", length + 1);
                        editor.putString("time" + length, time);
                        editor.putBoolean("state" + length, true);
                        editor.commit();
                        setAlarmManager(alarmManager, length, firstTime);
                        alarm_clock_times.add(alarmClockInfo);
                        alarmClockAdapter.notifyDataSetChanged();
                        timePickerDialog.dismiss();
                    }
                });
                timePickerDialog.setRightBtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePickerDialog.dismiss();
                    }
                });
                timePickerDialog.show();
            }
        });


    }

    private long setFirstTime(int hourOffset, int minuteOffset) {
        long firstTime = System.currentTimeMillis();
        int hour = 60 * 60 * 1000;
        int minute = 60 * 1000;
        if (hourOffset < 0) {
            if (minuteOffset < 0) {
                firstTime += 24 * hour - hourOffset * hour - minuteOffset * minute;
            } else {
                firstTime += 24 * hour - hourOffset * hour + minuteOffset * minute;
            }
        } else {
            if (minuteOffset < 0) {
                firstTime += hourOffset * hour - minuteOffset * minute;
            } else {
                firstTime += hourOffset * hour + minuteOffset * minute;
            }
        }
        return firstTime;
    }

    private String fillZero(String str) {
        if (str.length() < 2) {
            str = "0" + str;
        }
        return str;
    }

    private String getTime(MyTimePickerDialog timePickerDialog, int hourOffset, int minuteOffset) {
        String time = "";
        if (timePickerDialog.getHour() != 0 && timePickerDialog.getMinute() != 0) {
            String hourStr = String.valueOf(timePickerDialog.getHour());
            String minuteStr = String.valueOf(timePickerDialog.getMinute());
            hourStr = fillZero(hourStr);
            minuteStr = fillZero(minuteStr);
            time = hourStr + ":" + minuteStr;
        } else if (timePickerDialog.getHour() == 0 && timePickerDialog.getMinute() == 0) {
            String hourStr = String.valueOf(Math.abs(hourOffset));
            String minuteStr = String.valueOf(Math.abs(minuteOffset));
            hourStr = fillZero(hourStr);
            minuteStr = fillZero(minuteStr);
            time = hourStr + ":" + minuteStr;
        }
        return time;
    }

    private void setAlarmManager(AlarmManager alarmManager, int position, long firstTime) {
        Intent intent = new Intent(AlarmClockActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(AlarmClockActivity.this, position, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, 24*60*60*1000, pi);
    }


    class AlarmClockAdapter extends BaseAdapter {
        private TextView alarm_clock_time;
        private ImageView alarm_clock_switch_btn;
        private RelativeLayout alarm_clock_area;

        @Override
        public int getCount() {
            return alarm_clock_times.size();
        }

        @Override
        public Object getItem(int position) {
            return alarm_clock_times.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(AlarmClockActivity.this).inflate(R.layout.item_alarm_clock, null);
            alarm_clock_time = (TextView) convertView.findViewById(R.id.alarm_clock_time);
            alarm_clock_switch_btn = (ImageView) convertView.findViewById(R.id.alarm_clock_switch_btn);
            alarm_clock_area = (RelativeLayout) convertView.findViewById(R.id.alarm_clock_area);
            alarm_clock_time.setText(alarm_clock_times.get(position).getTime());
            if (alarm_clock_times.get(position).getState()) {
                alarm_clock_switch_btn.setImageResource(R.mipmap.switch_on);
            } else {
                alarm_clock_switch_btn.setImageResource(R.mipmap.switch_off);
            }
            final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarm_clock_switch_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alarm_clock_times.get(position).getState()) {
                        alarm_clock_times.get(position).setState(false);
                        editor.putBoolean("state" + position, false);
                        editor.commit();
                        Intent intent = new Intent(AlarmClockActivity.this, AlarmReceiver.class);
                        PendingIntent pi = PendingIntent.getBroadcast(AlarmClockActivity.this, position, intent, 0);
                        alarmManager.cancel(pi);
                    } else {
                        alarm_clock_times.get(position).setState(true);
                        editor.putBoolean("state" + position, true);
                        editor.commit();
                        String time = preferences.getString("time" + position, null);
                        int hour = Integer.parseInt(time.substring(0, 2));
                        int minute = Integer.parseInt(time.substring(3, 5));
                        Calendar currentTime = Calendar.getInstance();
                        int hourOffset = hour - currentTime.get(Calendar.HOUR_OF_DAY);
                        int minuteOffset = minute - currentTime.get(Calendar.MINUTE);
                        long firstTime = setFirstTime(hourOffset, minuteOffset);

                        setAlarmManager(alarmManager, position, firstTime);

                    }
                    notifyDataSetChanged();
                }
            });

            alarm_clock_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(AlarmClockActivity.this);
                    String timeStr = preferences.getString("time" + position, null);
                    final int hour = Integer.parseInt(timeStr.substring(0, 2));
                    final int minute = Integer.parseInt(timeStr.substring(3, 5));
                    timePickerDialog.setTimeFromSave(hour, minute);
                    timePickerDialog.setLeftBtnOnClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar currentTime = Calendar.getInstance();
                            int hourOffset = timePickerDialog.getHour() - currentTime.get(Calendar.HOUR_OF_DAY);
                            int minuteOffset = timePickerDialog.getMinute() - currentTime.get(Calendar.MINUTE);
                            long firstTime = setFirstTime(hourOffset, minuteOffset);
                            String time = getTime(timePickerDialog, hourOffset, minuteOffset);
                            alarm_clock_times.get(position).setTime(time);
                            editor.putString("time" + position, time);
                            editor.putBoolean("state" + position, true);
                            editor.commit();
                            setAlarmManager(alarmManager, position, firstTime);
                            Util.println(alarm_clock_times.get(position).getState());
                            if (!alarm_clock_times.get(position).getState()) {
                                alarm_clock_times.get(position).setState(true);
                                alarm_clock_switch_btn.setImageResource(R.mipmap.switch_on);
                            }
                            alarmClockAdapter.notifyDataSetChanged();
                            timePickerDialog.dismiss();
                        }
                    });
                    timePickerDialog.setRightBtnOnClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            timePickerDialog.dismiss();
                        }
                    });
                    timePickerDialog.show();
                }
            });

            return convertView;
        }
    }
}
