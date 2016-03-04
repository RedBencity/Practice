package ben.practice.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ben.practice.R;
import ben.practice.entity.PracticePointInfo;
import ben.practice.utils.NetUtil;
import ben.practice.utils.Util;

//科目
public class SubjectActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView head_title;
    private ListView practice_point_list;
    private List<PracticePointInfo> practicePointInfos;
    private String[] point;
    private String[] pinyin;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        initView();
        System.out.print("");
    }

    private void initView() {
        setToolbar();
        setListAdapter();

    }

    private void setToolbar() {
        Intent intent = getIntent();
        View view = findViewById(R.id.subject_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsRelative(0, 0);
        head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText(intent.getStringExtra("subject_name"));
        subject = intent.getStringExtra("subject_name");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setListAdapter() {
        practice_point_list = (ListView) findViewById(R.id.practice_point_list);
        practicePointInfos = new ArrayList<PracticePointInfo>();
        if (subject.equals("英语")) {
            point = new String[]{"副词", "介词", "时态","形容词"};
            pinyin = new String[]{"fuci","jieci","shitai","xingrongci"};
        } else if (subject.equals("语文")) {
            point = new String[]{"副词", "介词"};
        }else if (subject.equals("数学")) {
            point = new String[]{"副词", "介词", "时态"};
        }
        for (int i = 0; i < point.length; i++) {
            practicePointInfos.add(new PracticePointInfo(point[i]));
        }

        practice_point_list.setAdapter(new PracticePointAdapter(practicePointInfos));
    }




    class PracticePointAdapter extends BaseAdapter {

        private List<PracticePointInfo> practicePointInfoss;

        public PracticePointAdapter(List<PracticePointInfo> practicePointInfoss) {
            this.practicePointInfoss = practicePointInfoss;
        }

        @Override
        public int getCount() {
            return practicePointInfoss.size();
        }

        @Override
        public Object getItem(int position) {
            return practicePointInfoss.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_practice_point, null);
                viewHolder.point_name = (TextView) convertView.findViewById(R.id.point_name);
                viewHolder.point_begin_btn = (ImageView) convertView.findViewById(R.id.point_begin_btn);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.point_name.setText(practicePointInfoss.get(position).getName());
            viewHolder.point_begin_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(SubjectActivity.this, QuestionActivity.class);
                    intent.putExtra("subject",subject);
                    intent.putExtra("point_name",pinyin[position]);
                    intent.putExtra("point_name_",practicePointInfoss.get(position).getName());
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        public TextView point_name;
        public ImageView point_begin_btn;
    }



}
