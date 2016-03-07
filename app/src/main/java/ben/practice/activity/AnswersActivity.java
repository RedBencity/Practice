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

//答题卡
public class AnswersActivity extends AppCompatActivity {

    private GridView answers_gridview;
    private int question_position_resultCode = 0x123;
    private Toolbar toolbar;
    private TextView submit_result_btn;
    private int[] results;
    private int[] right_results_from_question;
    private int[] right_results_from_analyze;
    private String[] analyazes;
    private String question_number;
    private String subject;
    private String point_name;
    private ArrayList<Question> questionArrayList_from_question;
    private ArrayList<Question> questionArrayList_from_analyze;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        initView();
        getData();
    }

    private void initView() {
        answers_gridview = (GridView) findViewById(R.id.answers_gridview);
        setToolbar();
        submit_result_btn = (TextView) findViewById(R.id.submit_result_btn);
        preferences = getSharedPreferences("constants", MODE_PRIVATE);
    }

    private void getData() {
        final Intent intent = getIntent();
        results = intent.getIntArrayExtra("results");
        question_number = intent.getStringExtra("question_number");
        questionArrayList_from_question = intent.getParcelableArrayListExtra("questionArrayList_from_question");
        questionArrayList_from_analyze = intent.getParcelableArrayListExtra("questionArrayList_from_analyze");
        if (questionArrayList_from_question!=null) {
            subject = questionArrayList_from_question.get(0).getSubject();
            point_name = questionArrayList_from_question.get(0).getPoint();
        }



        setGridView();
        submit_result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadQuestionNumber();
                Intent toResultIntent = new Intent(AnswersActivity.this, ResultActivity.class);
                toResultIntent.putExtra("results", results);
                Util.println(AnswersActivity.this,results.length);
                toResultIntent.putParcelableArrayListExtra("questionArrayList",questionArrayList_from_question);
                QuestionActivity.instance.finish();
                startActivity(toResultIntent);
                finish();
            }
        });
        if (questionArrayList_from_analyze !=null){
            subject = questionArrayList_from_analyze.get(0).getSubject();
            point_name = questionArrayList_from_analyze.get(0).getPoint();
            right_results_from_analyze = new int[questionArrayList_from_analyze.size()];
            submit_result_btn.setVisibility(View.INVISIBLE);
            for (int i = 0; i < questionArrayList_from_analyze.size(); i++) {
                if (questionArrayList_from_analyze.get(i).getAnswer().equals("A")) {
                    right_results_from_analyze[i] = 1;
                } else if (questionArrayList_from_analyze.get(i).getAnswer().equals("B")) {
                    right_results_from_analyze[i] = 2;
                } else if (questionArrayList_from_analyze.get(i).getAnswer().equals("C")) {
                    right_results_from_analyze[i] = 3;
                } else if (questionArrayList_from_analyze.get(i).getAnswer().equals("D")) {
                    right_results_from_analyze[i] = 4;
                }
            }
        }
    }



    private void uploadQuestionNumber() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = NetUtil.URL + "/servlet/QuestionServer";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                    System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Util.setToast(AnswersActivity.this, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "uploadQuestionNumber");
                map.put("phone", preferences.getString("phone", "default"));
                map.put("subject", subject);
                map.put("point", point_name);
                map.put("questionNumber", question_number);

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void setToolbar() {
        View view = findViewById(R.id.answers_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsRelative(0, 0);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("答题卡");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
            }
        });
    }

    private void setGridView() {
        answers_gridview.setAdapter(new AnswersAdapter());
    }

    class AnswersAdapter extends BaseAdapter {

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
            convertView = LayoutInflater.from(AnswersActivity.this).inflate(R.layout.item_answers, null);
//            ImageView answers_position_bg = (ImageView) convertView.findViewById(R.id.answers_position_bg);
            TextView answers_position = (TextView) convertView.findViewById(R.id.answers_position);
            answers_position.setText((position + 1) + "");
            if (results[position] != 0) {
                answers_position.setBackgroundResource(R.mipmap.answer_btn_answered);
                answers_position.setTextColor(getResources().getColor(R.color.white));
            }

            if (right_results_from_analyze != null) {

                if (results[position] == right_results_from_analyze[position]) {
                    answers_position.setTextColor(getResources().getColor(R.color.white));
                    answers_position.setBackgroundResource(R.mipmap.answer_btn_right);
                } else if (results[position] != right_results_from_analyze[position] && results[position] != 0) {
                    answers_position.setTextColor(getResources().getColor(R.color.white));
                    answers_position.setBackgroundResource(R.mipmap.answer_btn_wrong);
                }


            }


            answers_position.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    intent.putExtra("question_position", position);
                    setResult(question_position_resultCode, intent);
                    AnswersActivity.this.finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);

                }

            });
            return convertView;
        }
    }
}
