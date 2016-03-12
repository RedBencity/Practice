package ben.practice.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ben.practice.R;

public class CollectActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView collect_listview;
    private String subjects[] = new String[]{"语文", "数学", "英语"};
    private int[] subject_icons = new int[]{R.mipmap.icon_marked_question_yuwen,
            R.mipmap.icon_marked_question_shuxue, R.mipmap.icon_marked_question_yingyu};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        collect_listview = (ListView) findViewById(R.id.collect_listview);

    }

    private void getData() {
        collect_listview.setAdapter(new CollectSubjectAdapter());

        collect_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(CollectActivity.this, MarkActivity.class);
                intent.putExtra("subject",subjects[position]);
                startActivity(intent);
            }
        });

    }

    private void setToolbar() {
        View view = findViewById(R.id.collect_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("收藏");
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

    class CollectSubjectAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return subjects.length;
        }

        @Override
        public Object getItem(int position) {
            return subjects[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(CollectActivity.this).inflate(R.layout.item_collect_subject, null);
            TextView subject_name = (TextView) convertView.findViewById(R.id.subject_name);
            ImageView subject_icon = (ImageView)convertView.findViewById(R.id.subject_icon);
            subject_name.setText(subjects[position]);
            subject_icon.setBackgroundResource(subject_icons[position]);
            return convertView;
        }
    }
}
