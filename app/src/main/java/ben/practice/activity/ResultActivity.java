package ben.practice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import ben.practice.R;

public class ResultActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView head_title;
    private int[] results;
    private  int[] right_results;
    private String[] analyazes;
    private GridView result_gridview;
    private TextView practice_style;
    private TextView cut_off_time;
    private TextView right_count;
    private TextView total_count;
    private String point_name;
    private int rightCount =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();
        getData();
    }

    private void initView(){
        setToolbar();
        result_gridview = (GridView)findViewById(R.id.result_gridview);
        practice_style = (TextView)findViewById(R.id.practice_style);
        cut_off_time = (TextView)findViewById(R.id.cut_off_time);
        right_count = (TextView)findViewById(R.id.right_count);
        total_count = (TextView)findViewById(R.id.total_count);
    }

    private void getData(){
        Intent intent = getIntent();
        results = intent.getIntArrayExtra("results");
        right_results = intent.getIntArrayExtra("right_results");
        analyazes = intent.getStringArrayExtra("analyzes");
        point_name = intent.getStringExtra("point_name_chinese");
        practice_style.setText("练习类型："+point_name);
        cut_off_time.setText("交卷时间："+getTime());

        for (int i=0;i<results.length;i++){
            if(results[i]== right_results[i]){
                rightCount++;
            }
        }
        right_count.setText(rightCount+"");
        total_count.setText("道/"+results.length+"道");
        setGridView();

    }

    private void setToolbar(){
        View view = findViewById(R.id.result_toolbar);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsRelative(0, 0);
        head_title = (TextView)view.findViewById(R.id.head_title);
        head_title.setText("练习报告");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getTime(){
        Time time = new Time("Asia/Shanghai");
        time.setToNow();
        int year = time.year;
        int month = time.month+1;
        int day = time.monthDay;
        int hour = time.hour;
        int second = time.second;
        return year+"."+month+"."+day+"   "+hour+"."+second;
    }



    private void setGridView(){
        result_gridview.setAdapter(new ResultAdapter());
    }

    class ResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return results.length;
        }

        @Override
        public Object getItem(int position) {
            return results[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(ResultActivity.this).inflate(R.layout.item_result, null);
            TextView result_position = (TextView) convertView.findViewById(R.id.result_position);
            result_position.setText((position + 1) + "");
           if(results[position]== right_results[position]){
               result_position.setBackgroundResource(R.mipmap.answer_btn_right);
               result_position.setTextColor(getResources().getColor(R.color.white));
           }else if (results[position]==0){
               result_position.setBackgroundResource(R.mipmap.answer_btn_cant_be_answered);
               result_position.setTextColor(getResources().getColor(R.color.colorPrimary));
           }else if (results[position]!= right_results[position]){
               result_position.setBackgroundResource(R.mipmap.answer_btn_wrong);
               result_position.setTextColor(getResources().getColor(R.color.white));
           }

           result_position.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(ResultActivity.this,AnalyzeActivity.class);
                   intent.putExtra("analyze","analyze");
                   intent.putExtra("point_name",point_name);
                   intent.putExtra("position",position);
                   intent.putExtra("right_results",right_results);
                   intent.putExtra("results",results);
                   startActivity(intent);
               }
           });

            return convertView;
        }
    }
}
