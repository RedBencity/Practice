package ben.practice.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import ben.practice.entity.Question;
import ben.practice.utils.NetUtil;
import ben.practice.utils.Util;

public class MarkActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView mark_listview;
    private List<Question> questions;
    private SharedPreferences preferences;
    private String subject;
    private MarkAdapter markAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        mark_listview = (ListView) findViewById(R.id.mark_listview);
        preferences = getSharedPreferences("constants", MODE_PRIVATE);

    }

    private void getData() {
        Intent intent = getIntent();
        subject = intent.getStringExtra("subject");
        getCollectQuestion();
        questions = new ArrayList<Question>();
        markAdapter = new MarkAdapter();
        mark_listview.setAdapter(markAdapter);
    }

    private void setToolbar() {
        View view = findViewById(R.id.mark_toolbar);
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

    private void getCollectQuestion() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetUtil.URL + "/servlet/QuestionServer";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                    System.out.println(response);
                if (!response.equals("no")) {
                    Question question;
                    String[] str = response.split("#");
                    for (int i = 0; i < str.length; i++) {
                        question = new Question();
                        String[] strings = str[i].split("@");
                        question.setTitle(strings[0]);
                        question.setA(strings[1]);
                        question.setB(strings[2]);
                        question.setC(strings[3]);
                        question.setD(strings[4]);
                        question.setAnswer(strings[5]);
                        question.setAnalyze(strings[6]);
                        questions.add(question);
                    }
                    markAdapter.notifyDataSetChanged();
                }else{
                    Util.setToast(MarkActivity.this,"没有收藏任何题目");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Util.setToast(MarkActivity.this, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "getCollectQuestion");
                map.put("phone", preferences.getString("phone", "default"));
                map.put("subject", subject);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    class MarkAdapter extends BaseAdapter {
        private TextView question_title, option_a, option_b, option_c, option_d;
        private TextView option_a_content, option_b_content, option_c_content, option_d_content;
        private TextView analyze_text;


        @Override
        public int getCount() {
            return questions.size();
        }

        @Override
        public Object getItem(int position) {
            return questions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(MarkActivity.this).inflate(R.layout.item_mark, null);
            question_title = (TextView) convertView.findViewById(R.id.question_title);
            option_a = (TextView) convertView.findViewById(R.id.option_a);
            option_b = (TextView) convertView.findViewById(R.id.option_b);
            option_c = (TextView) convertView.findViewById(R.id.option_c);
            option_d = (TextView) convertView.findViewById(R.id.option_d);
            option_a_content = (TextView) convertView.findViewById(R.id.option_a_content);
            option_b_content = (TextView) convertView.findViewById(R.id.option_b_content);
            option_c_content = (TextView) convertView.findViewById(R.id.option_c_content);
            option_d_content = (TextView) convertView.findViewById(R.id.option_d_content);
            analyze_text = (TextView) convertView.findViewById(R.id.analyze_text);
            question_title.setText(questions.get(position).getTitle());
            option_a_content.setText(questions.get(position).getA());
            option_b_content.setText(questions.get(position).getB());
            option_c_content.setText(questions.get(position).getC());
            option_d_content.setText(questions.get(position).getD());
            analyze_text.setText(questions.get(position).getAnalyze());
            return convertView;
        }
    }
}
