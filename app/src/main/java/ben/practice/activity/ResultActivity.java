package ben.practice.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ben.practice.R;
import ben.practice.entity.Question;
import ben.practice.utils.NetUtil;
import ben.practice.utils.Util;

public class ResultActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView head_title;
    private int[] results;
    private  int[] right_results;
    private GridView result_gridview;
    private TextView practice_style;
    private TextView cut_off_time;
    private TextView right_count;
    private TextView total_count;
    private String point_name;
    private ArrayList<Question> questionArrayList;
    private int rightCount =0;
    private SharedPreferences preferences;

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
        preferences = getSharedPreferences("constants", MODE_PRIVATE);
    }

    private void getData(){
        Intent intent = getIntent();
        results = intent.getIntArrayExtra("results");
        questionArrayList = intent.getParcelableArrayListExtra("questionArrayList");
        right_results =new int[questionArrayList.size()];
        point_name = questionArrayList.get(0).getPoint();
        for (int i = 0; i < questionArrayList.size(); i++) {
            if (questionArrayList.get(i).getAnswer().equals("A")) {
                right_results[i] = 1;
            } else if (questionArrayList.get(i).getAnswer().equals("B")) {
                right_results[i] = 2;
            } else if (questionArrayList.get(i).getAnswer().equals("C")) {
                right_results[i] = 3;
            } else if (questionArrayList.get(i).getAnswer().equals("D")) {
                right_results[i] = 4;
            }
        }
        practice_style.setText("练习类型："+point_name);
        cut_off_time.setText("交卷时间："+getTime());
        Util.println(ResultActivity.this,results.length);
        for (int i=0;i<results.length;i++){
            if(results[i]== right_results[i]){
                rightCount++;
            }
        }
        right_count.setText(rightCount+"");
        total_count.setText("道/"+results.length+"道");
        setGridView();
        uploadQuestionCondition();

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
        int minute = time.minute;
        int second = time.second;
        return year+"."+month+"."+day+"   "+hour+":"+minute+":"+second;
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
                   intent.putExtra("position",position);
                   intent.putExtra("results",results);
                   intent.putParcelableArrayListExtra("questionArrayList",questionArrayList);
                   startActivity(intent);
               }
           });
            return convertView;
        }
    }

    private void uploadQuestionCondition(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetUtil.URL + "/servlet/QuestionServer";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                    System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.setToast(ResultActivity.this, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "uploadQuestionCondition");
                map.put("phone", preferences.getString("phone", "default"));
                map.put("subject",questionArrayList.get(0).getSubject());
                map.put("rightCount",String.valueOf(rightCount));
                map.put("totalCount",String.valueOf(questionArrayList.size()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
