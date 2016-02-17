package ben.practice.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ben.practice.R;

public class PublicActivity extends AppCompatActivity {
   private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        initView();
        getData();
    }

    private void getData() {

    }

    private void initView() {
        setToolbar();
    }

    private void setToolbar() {
        View view = findViewById(R.id.public_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("个人");
//        ImageView head_right_btn = (ImageView)view.findViewById(R.id.head_right_btn);
//        head_right_btn.setVisibility(View.VISIBLE);
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
