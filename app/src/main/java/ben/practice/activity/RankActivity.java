package ben.practice.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import ben.practice.R;
import ben.practice.adapter.ListAdapter;

public class RankActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView rank_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
    }

    private void getData(){
        rank_listview = (ListView)findViewById(R.id.rank_listview);
        int[] pootos = new int[]{R.mipmap.icon_default_avatar,R.mipmap.icon_default_avatar,R.mipmap.icon_default_avatar,R.mipmap.icon_default_avatar};
        String[] usernames =new String[]{"red","blue","white","blank"};
        rank_listview.setAdapter(new ListAdapter(this,pootos,usernames));
    }

    private void setToolbar() {
        View view = findViewById(R.id.rank_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("排行榜");
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
