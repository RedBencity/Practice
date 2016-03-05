package ben.practice.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.util.prefs.Preferences;

import ben.practice.R;
import ben.practice.entity.Question;
import ben.practice.utils.NetUtil;
import ben.practice.utils.RestDialog;
import ben.practice.utils.Util;

//做题
public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private ArrayList<View> pageViews;
    private Toolbar toolbar;
    private ImageView bar_answers;
    private ImageView bar_scratch;
    private Chronometer bar_time;
    private ImageView bar_time_bg;
    public static QuestionActivity instance = null;
    private int questionCount = 10;
    private int[] results;
    private int[] right_results;
    private String[] analyazes;
    private int answers_requestCode = 0x123;
    private int question_position_resultCode = 0x123;
    private String point_name;
    private String point_name_chinese;
    String question_number;
    String subject;
    private ArrayList<Question> questionArrayList;


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initView();
        getData();

    }

    private void initView() {
        instance = this;
        setToolbar();
        preferences = getSharedPreferences("constants", MODE_PRIVATE);
        editor = preferences.edit();
        pageViews = new ArrayList<View>();
        viewPager = (ViewPager) findViewById(R.id.viewpager);

    }

    private void getData() {
//        results = new int[questionCount];
//        right_results = new int[questionCount];
//        analyazes = new String[questionCount];
        Intent intent = getIntent();
        subject = intent.getStringExtra("subject");
        point_name = intent.getStringExtra("point_name");
        point_name_chinese = intent.getStringExtra("point_name_chinese");
        getQuestion();
//        setViewPager();


    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.bar_question_more));
        setSupportActionBar(toolbar);
        bar_scratch = (ImageView) findViewById(R.id.bar_scratch);
        bar_scratch.setOnClickListener(this);
        bar_answers = (ImageView) findViewById(R.id.bar_answers);
        bar_answers.setOnClickListener(this);

        setBarTime();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.collect_item:
                        break;
                    case R.id.share_item:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == answers_requestCode && resultCode == question_position_resultCode) {
            int position = data.getIntExtra("question_position", 0);
            viewPager.setCurrentItem(position);
        }
    }

    private void setBarTime() {
        bar_time_bg = (ImageView) findViewById(R.id.bar_time_bg);
        bar_time = (Chronometer) findViewById(R.id.bar_time);
        bar_time.setBase(SystemClock.elapsedRealtime());
        bar_time.setFormat("0%s");
        bar_time.start();
        bar_time.setOnClickListener(new View.OnClickListener() {
            long pauseTime;

            @Override
            public void onClick(View v) {
                bar_time.stop();
                pauseTime = SystemClock.elapsedRealtime() - bar_time.getBase();
                bar_time_bg.setImageResource(R.mipmap.bar_time_checked);
                final RestDialog restDialog = new RestDialog(QuestionActivity.this);
                restDialog.setRestNavigationOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bar_time.setBase(SystemClock.elapsedRealtime() - pauseTime);
                        bar_time.start();
                        restDialog.dismiss();
                        bar_time_bg.setImageResource(R.mipmap.bar_time);
                    }
                });
                restDialog.show();
            }
        });
        bar_time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (SystemClock.elapsedRealtime() - bar_time.getBase() < 1000 * 599) {
                    bar_time.setFormat("0%s");
                } else {
                    bar_time.setFormat("%s");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setViewPager() {
        for (int i = 0; i < questionCount + 1; i++) {
            if (i != questionCount) {
                View view = LayoutInflater.from(this).inflate(R.layout.viewpager_question_card, null);
                TextView question_position = (TextView) view.findViewById(R.id.question_position);
                TextView question_total = (TextView) view.findViewById(R.id.question_total);
                TextView paractice_style = (TextView) view.findViewById(R.id.practice_style);
                paractice_style.setText(point_name_chinese);
                question_position.setText(i + 1 + "");
                question_total.setText("/" + questionCount);
                pageViews.add(view);
            } else if (i == questionCount) {
                View view = LayoutInflater.from(this).inflate(R.layout.viewpager_answers_card, null);
                pageViews.add(view);
            }
        }
        viewPager.setAdapter(new QuestionAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_answers:
                if (currentPosition != questionCount) {
                    for (int i = 0; i < questionArrayList.size(); i++) {
                        analyazes[i] = questionArrayList.get(i).getAnalyze();
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
                    Intent intent = new Intent(QuestionActivity.this, AnswersActivity.class);
                    intent.putExtra("results", results);
                    intent.putExtra("right_results", right_results);
                    intent.putExtra("analyzes", analyazes);
                    intent.putExtra("question_number", question_number);
                    intent.putExtra("questionCount", questionCount);
                    intent.putExtra("point_name_chinese", point_name_chinese);
                    startActivityForResult(intent, answers_requestCode);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                } else if (currentPosition == questionCount) {

                }
                break;
            case R.id.bar_scratch:
                if (currentPosition != questionCount) {
                    Intent intent = new Intent(QuestionActivity.this, ScratchActivity.class);
                    startActivity(intent);
                } else if (currentPosition == questionCount) {

                }
                break;
        }
    }

    private int currentPosition;

    class QuestionAdapter extends PagerAdapter implements View.OnClickListener {

        private RelativeLayout option_a_area, option_b_area, option_c_area, option_d_area;
        private TextView question_title, option_a, option_b, option_c, option_d;
        private GridView answers_card;
        private TextView submit_result_btn;
        private TextView option_a_content, option_b_content, option_c_content, option_d_content;

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            ((ViewPager) container).removeView(pageViews.get(position));
        }

        //每次滑动的时候生成的组件
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(pageViews.get(position));
            return pageViews.get(position);
        }


        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentPosition = position;
            View view = (View) object;
            if (position != questionCount) {
                bar_scratch.setImageResource(R.drawable.selector_bar_scratch);
                bar_answers.setImageResource(R.drawable.selector_bar_answers);
                option_a_area = (RelativeLayout) view.findViewById(R.id.option_a_area);
                option_b_area = (RelativeLayout) view.findViewById(R.id.option_b_area);
                option_c_area = (RelativeLayout) view.findViewById(R.id.option_c_area);
                option_d_area = (RelativeLayout) view.findViewById(R.id.option_d_area);
                question_title = (TextView) view.findViewById(R.id.question_title);
                option_a = (TextView) view.findViewById(R.id.option_a);
                option_b = (TextView) view.findViewById(R.id.option_b);
                option_c = (TextView) view.findViewById(R.id.option_c);
                option_d = (TextView) view.findViewById(R.id.option_d);
                option_a_content = (TextView) view.findViewById(R.id.option_a_content);
                option_b_content = (TextView) view.findViewById(R.id.option_b_content);
                option_c_content = (TextView) view.findViewById(R.id.option_c_content);
                option_d_content = (TextView) view.findViewById(R.id.option_d_content);
                option_a_area.setOnClickListener(this);
                option_b_area.setOnClickListener(this);
                option_c_area.setOnClickListener(this);
                option_d_area.setOnClickListener(this);

                question_title.setText(questionArrayList.get(position).getTitle());
                option_a_content.setText(questionArrayList.get(position).getA());
                option_b_content.setText(questionArrayList.get(position).getB());
                option_c_content.setText(questionArrayList.get(position).getC());
                option_d_content.setText(questionArrayList.get(position).getD());


            } else if (position == questionCount) {
                answers_card = (GridView) view.findViewById(R.id.answers_card_gridview);
                answers_card.setAdapter(new AnswersAdapter());
                bar_scratch.setImageResource(R.mipmap.bar_scratch_disable);
                bar_answers.setImageResource(R.mipmap.bar_answers_disable);
                submit_result_btn = (TextView) view.findViewById(R.id.submit_result_btn);
                submit_result_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < questionArrayList.size(); i++) {
                            analyazes[i] = questionArrayList.get(i).getAnalyze();
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
                        uploadQuestionNumber();
                        Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
                        intent.putExtra("results", results);
                        intent.putExtra("right_results", right_results);
                        intent.putExtra("analyzes", analyazes);
                        intent.putExtra("point_name_chinese", point_name_chinese);
                        startActivity(intent);
                        finish();
                    }
                });


            }
        }

        private void uploadQuestionNumber() {
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

                    Util.setToast(QuestionActivity.this, "服务器异常!");
                    Log.e("TAG", error.getMessage(), error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    //在这里设置需要post的参数
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("requestType", "uploadQuestionNumber");
                    map.put("phone", preferences.getString("phone", "default"));
                    map.put("point", point_name);
                    map.put("questionNumber", question_number);

                    return map;
                }
            };
            requestQueue.add(stringRequest);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.option_a_area:
                    clearChoice();
                    option_a.setTextColor(getResources().getColor(R.color.white));
                    option_a.setBackgroundResource(R.mipmap.option_btn_single_checked);
//                    isAnswers[currentPosition] = true;
                    results[currentPosition] = 1;
                    viewPager.setCurrentItem(currentPosition + 1);
                    break;
                case R.id.option_b_area:
                    clearChoice();
                    option_b.setTextColor(getResources().getColor(R.color.white));
                    option_b.setBackgroundResource(R.mipmap.option_btn_single_checked);
//                    isAnswers[currentPosition] = true;
                    results[currentPosition] = 2;
                    viewPager.setCurrentItem(currentPosition + 1);

                    break;
                case R.id.option_c_area:
                    clearChoice();
                    option_c.setTextColor(getResources().getColor(R.color.white));
                    option_c.setBackgroundResource(R.mipmap.option_btn_single_checked);
//                    isAnswers[currentPosition] = true;
                    results[currentPosition] = 3;
                    viewPager.setCurrentItem(currentPosition + 1);

                    break;
                case R.id.option_d_area:
                    clearChoice();
                    option_d.setTextColor(getResources().getColor(R.color.white));
                    option_d.setBackgroundResource(R.mipmap.option_btn_single_checked);
//                    isAnswers[currentPosition] = true;
                    results[currentPosition] = 4;
                    viewPager.setCurrentItem(currentPosition + 1);

                    break;
            }
        }

        private void clearChoice() {
            option_a.setTextColor(getResources().getColor(R.color.colorPrimary));
            option_a.setBackgroundResource(R.mipmap.option_btn_single_normal);
            option_b.setTextColor(getResources().getColor(R.color.colorPrimary));
            option_b.setBackgroundResource(R.mipmap.option_btn_single_normal);
            option_c.setTextColor(getResources().getColor(R.color.colorPrimary));
            option_c.setBackgroundResource(R.mipmap.option_btn_single_normal);
            option_d.setTextColor(getResources().getColor(R.color.colorPrimary));
            option_d.setBackgroundResource(R.mipmap.option_btn_single_normal);
        }

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
            convertView = LayoutInflater.from(QuestionActivity.this).inflate(R.layout.item_answers, null);
//            ImageView answers_position_bg = (ImageView) convertView.findViewById(R.id.answers_position_bg);
            TextView answers_position = (TextView) convertView.findViewById(R.id.answers_position);
            answers_position.setText((position + 1) + "");
            if (results[position] != 0) {
                answers_position.setBackgroundResource(R.mipmap.answer_btn_answered);
                answers_position.setTextColor(getResources().getColor(R.color.white));
            }
            answers_position.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(position);
                }
            });
            return convertView;
        }
    }

    private void getQuestion() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetUtil.URL + "/servlet/QuestionServer";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                System.out.println(response);

                if(response.equals("no_file")){
                    QuestionActivity.this.finish();
                    Util.setToast(QuestionActivity.this, "暂时没有题目！");
                }else if (response.equals("all_do")){
                    QuestionActivity.this.finish();
                    Util.setToast(QuestionActivity.this, "您已做完该题型所有题目！");
                }else{
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
                        question = new Question(title, a, b, c, d, answer, analyze);
                        questionArrayList.add(question);
                    }
                    question_number = questions[questions.length - 1];
                    questionCount = questionArrayList.size();
                    results = new int[questionCount];
                    right_results = new int[questionCount];
                    analyazes = new String[questionCount];
                    setViewPager();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Util.setToast(QuestionActivity.this, "服务器异常!");
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
