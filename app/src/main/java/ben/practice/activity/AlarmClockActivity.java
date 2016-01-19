package ben.practice.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import ben.practice.MainActivity;
import ben.practice.R;
import ben.practice.adapter.ListAdapter;
import ben.practice.broadcast.AlarmReceiver;
import ben.practice.utils.Util;

public class AlarmClockActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView alarm_clock_listview;

    private List<String> alarm_clock_times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        alarm_clock_times = new ArrayList<String>();
    }
    private void getData(){
        alarm_clock_listview = (ListView)findViewById(R.id.alarm_clock_listview);

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

        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        head_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                // 弹出一个时间设置的对话框,供用户选择时间
                new TimePickerDialog(AlarmClockActivity.this, 0,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                Intent intent = new Intent(AlarmClockActivity.this, AlarmReceiver.class);
                                PendingIntent pi = PendingIntent.getBroadcast(AlarmClockActivity.this, 0, intent, 0);
                                //设置当前时间
                                Calendar c = Calendar.getInstance();
                                TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
                                c.setTimeZone(tz);
                                c.setTimeInMillis(System.currentTimeMillis());
                                // 根据用户选择的时间来设置Calendar对象
                                c.set(Calendar.HOUR, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                System.out.println(c.getTime());
                                // 设置AlarmManager在Calendar对应的时间启动Activity
                                 System.out.println(" aaa " + c.getTimeInMillis() +"  ");
                                System.out.println(" bbb "+System.currentTimeMillis());
                                System.out.println(" ccc "+(c.getTimeInMillis()-12*60*60*1000-System.currentTimeMillis()));
                                System.out.println(" ddd "+(c.getTimeInMillis()-SystemClock.elapsedRealtime()));
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()-12*60*60*1000, 1000, pi);
//                                alarmManager.set(AlarmManager.RTC_WAKEUP,
//                                        c.getTimeInMillis(), pi);
                                // 提示闹钟设置完毕
                                Toast.makeText(AlarmClockActivity.this, "闹钟设置完毕",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime
                        .get(Calendar.MINUTE), true).show();
            }
        });



    }

    class AlarmClockAdapter extends BaseAdapter{
        private TextView alarm_clock_time;
        private ImageView alarm_clock_switch_btn;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(AlarmClockActivity.this).inflate(R.layout.item_alarm_clock,null);
            alarm_clock_time = (TextView)convertView.findViewById(R.id.alarm_clock_time);
            alarm_clock_switch_btn = (ImageView)convertView.findViewById(R.id.alarm_clock_switch_btn);
            return convertView;
        }
    }
}
