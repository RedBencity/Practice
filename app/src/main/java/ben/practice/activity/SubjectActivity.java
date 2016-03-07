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
import ben.practice.entity.Question;
import ben.practice.utils.NetUtil;
import ben.practice.utils.Util;

//科目
public class SubjectActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView head_title;
    private ListView practice_point_list;
    private List<PracticePointInfo> practicePointInfos;
    private String[] point;
    private String subject;
    private SharedPreferences preferences;
    private ArrayList<Question> questionArrayList;
    private String question_number;

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
        subject = intent.getStringExtra("subject_name");
        head_title.setText(subject);
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        preferences = getSharedPreferences("constants", MODE_PRIVATE);
    }

    private void setListAdapter() {
        practice_point_list = (ListView) findViewById(R.id.practice_point_list);
        practicePointInfos = new ArrayList<PracticePointInfo>();
        if (subject.equals("英语")) {
            point = new String[]{"副词", "介词", "时态", "形容词"};
        } else if (subject.equals("语文")) {
            point = new String[]{"副词", "介词"};
        } else if (subject.equals("数学")) {
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
                    getQuestion(subject,practicePointInfoss.get(position).getName(),intent);
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        public TextView point_name;
        public ImageView point_begin_btn;
    }

    private void getQuestion(final String subject,final String point_name,final Intent intent) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetUtil.URL + "/servlet/QuestionServer";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                System.out.println(response);

                if (response.equals("no_file")) {
                    Util.setToast(SubjectActivity.this, "暂时没有题目！");
                } else if (response.equals("all_do")) {
                    Util.setToast(SubjectActivity.this, "您已做完该题型所有题目！");
                } else {
                    questionArrayList = new ArrayList<Question>();
                    Question question;
                    String[] questions = response.split("@");
                    for (int i = 0; i < questions.length - 1; i++) {
                        String[] list = questions[i].split("&");
                        for (int j = 0; j < list.length; j++) {
//                        System.out.println((j+1)+"------->"+list.length+"------>"+list[j]);
                        }
                        System.out.println();
                        String title = list[0];
                        String a = list[1];
                        String b = list[2];
                        String c = list[3];
                        String d = list[4];
                        String answer = list[5];
                        String analyze = list[6];
//                    System.out.println(title+" "+a+" "+b+" "+c+" "+d+" "+answer+" "+analyze);
                        question = new Question(title, a, b, c, d, answer, analyze, subject, point_name);
                        questionArrayList.add(question);
                    }
                    question_number = questions[questions.length - 1];
                    intent.putExtra("question_number", question_number);
                    intent.putParcelableArrayListExtra("questionArrayList",questionArrayList);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Util.setToast(SubjectActivity.this, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "getQuestion");
                map.put("phone", preferences.getString("phone", "default"));
                map.put("subject", subject);
                map.put("point", point_name);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


}
