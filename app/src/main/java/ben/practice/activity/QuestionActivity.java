package ben.practice.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ben.practice.R;
import ben.practice.utils.RestDialog;

public class QuestionActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<View> pageViews;
    private Toolbar toolbar;
    private ImageView bar_answers;
    private Chronometer bar_time;
    private ImageView bar_time_bg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initView();
        getData();

    }

    private void initView() {
        setToolbar();
        pageViews = new ArrayList<View>();
        viewPager = (ViewPager) findViewById(R.id.viewpager);

    }


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.bar_question_more));
        setSupportActionBar(toolbar);
        bar_answers = (ImageView) findViewById(R.id.bar_answers);
        bar_answers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        setBarTime();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.aaaa:
                        break;
                    case R.id.bbbb:
                        break;
                }
                return true;
            }
        });
    }

    private void setBarTime(){
        bar_time_bg = (ImageView)findViewById(R.id.bar_time_bg);
        bar_time = (Chronometer) findViewById(R.id.bar_time);
        bar_time.setBase(SystemClock.elapsedRealtime());
        bar_time.setFormat("0%s");
        bar_time.start();
        bar_time.setOnClickListener(new View.OnClickListener() {
            long pauseTime;
            @Override
            public void onClick(View v) {
                bar_time.stop();
                pauseTime = SystemClock.elapsedRealtime() - bar_time.getBase();
                bar_time_bg.setImageResource(R.mipmap.bar_time_checked);
                final RestDialog restDialog = new RestDialog(QuestionActivity.this);
                restDialog.setRestNavigationOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bar_time.setBase(SystemClock.elapsedRealtime() - pauseTime);
                        bar_time.start();
                        restDialog.dismiss();
                        bar_time_bg.setImageResource(R.mipmap.bar_time);
                    }
                });
                restDialog.show();
            }
        });
        bar_time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (SystemClock.elapsedRealtime() - bar_time.getBase() < 1000*599) {
                    bar_time.setFormat("0%s");
                }else {
                    bar_time.setFormat("%s");
                }
            }
        });
    }

    private void getData() {
        setViewPager();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setViewPager() {
        for (int i = 0; i < 10; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.question, null);
            TextView textView = (TextView) view.findViewById(R.id.question_position);
            textView.setText(i + 1 + "");
            pageViews.add(view);
        }
        viewPager.setAdapter(new QuestionAdapter());
    }

    class QuestionAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            ((ViewPager) container).removeView(pageViews.get(position));
        }

        //每次滑动的时候生成的组件
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(pageViews.get(position));
            return pageViews.get(position);
        }
    }
}
