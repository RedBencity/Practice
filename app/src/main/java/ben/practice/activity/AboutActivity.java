package ben.practice.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ben.practice.R;
import ben.practice.utils.Util;

public class AboutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        version = (TextView)findViewById(R.id.version);
    }

    private void getData() {
        version.setText("Version "+Util.getAppVersionName(AboutActivity.this)+" Author RedBencity");
    }

    private void setToolbar() {
        View view = findViewById(R.id.about_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("关于");
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
    }
}
