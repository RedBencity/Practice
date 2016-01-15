package ben.practice.activity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import ben.practice.R;
import ben.practice.entity.PracticePointInfo;
//科目
public class SubjectActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView head_title;
    private ListView practice_point_list;
    private List<PracticePointInfo> practicePointInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        initView();
        System.out.print("");
    }

    private void initView(){
        setToolbar();
        setListAdapter();

    }

    private void setToolbar(){
        Intent intent = getIntent();
        View view = findViewById(R.id.subject_toolbar);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsRelative(0, 0);
        head_title = (TextView)view.findViewById(R.id.head_title);
        head_title.setText(intent.getStringExtra("subject_name"));
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setListAdapter(){
        practice_point_list= (ListView)findViewById(R.id.practice_point_list);
        practicePointInfos = new ArrayList<PracticePointInfo>();
        practicePointInfos.add(new PracticePointInfo("听力"));
        practicePointInfos.add(new PracticePointInfo("听力"));
        practice_point_list.setAdapter(new PracticePointAdapter(practicePointInfos));
    }

    class PracticePointAdapter extends BaseAdapter {

        private List<PracticePointInfo> practicePointInfoss;

        public PracticePointAdapter(List<PracticePointInfo> practicePointInfoss){
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
            ViewHolder viewHolder =null;
            if(convertView ==null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_practice_point,null);
                viewHolder.point_name = (TextView)convertView.findViewById(R.id.point_name);
                viewHolder.point_begin_btn = (ImageView)convertView.findViewById(R.id.point_begin_btn);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.point_name.setText(practicePointInfoss.get(position).getName());
            viewHolder.point_begin_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(SubjectActivity.this,QuestionActivity.class);
                    intent.putExtra("point_name",practicePointInfoss.get(position).getName());
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class ViewHolder{
        public TextView point_name;
        public ImageView point_begin_btn;
    }
}
