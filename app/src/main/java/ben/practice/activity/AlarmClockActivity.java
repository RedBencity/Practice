package ben.practice.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ben.practice.R;
import ben.practice.adapter.ListAdapter;
import ben.practice.utils.Util;

public class AlarmClockActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView alarm_clock_listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
    }
    private void getData(){
        alarm_clock_listview = (ListView)findViewById(R.id.alarm_clock_listview);
    }

    private void setToolbar() {
        View view = findViewById(R.id.alarm_clock_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("每日练习提醒");
        ImageView head_right_btn = (ImageView)view.findViewById(R.id.head_right_btn);
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
        head_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
